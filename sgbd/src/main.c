#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "game.h"
#include "gameprinting.h"
#include "sequentialfile.h"

const int SIZE_LINE = 585; 

int main() {
	FILE *database = fopen("C:/Users/brenno/Documents/AEDSIII/sgbd/src/tmp/baseTest.csv", "r");   
    char *line = (char *) malloc(sizeof(char) * SIZE_LINE);

    while(fgets(line, SIZE_LINE, database) != NULL) {
        Game *game = (Game *) malloc(sizeof(Game));
        toRead(game, line);

        FILE *file = fopen("sequentialFile", "wb");
        sf_create(file, game);
        fclose(file);
        
        file = fopen("sequentialFile", "rb");
        sf_showFile(file);
        fclose(file);

        freeGame(game);
    }

    fclose(database);
    free(line);

	return 0;
}