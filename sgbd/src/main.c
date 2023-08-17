#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "game.h"
#include "gameprinting.h"
#include "sequentialfile.h"

const int SIZE_LINE = 585; 

int main() {
	FILE *database = fopen("C:/Users/brenno/Documents/AEDSIII/sgbd/src/tmp/baseTest.csv", "r");  
    FILE *file = fopen("sequentialFile", "wb");
    char *line = (char *) malloc(sizeof(char) * SIZE_LINE);

    while(fgets(line, SIZE_LINE, database) != NULL) {
        Game *game = (Game *) malloc(sizeof(Game));
        toRead(game, line);        
        sf_create(file, game);
        freeGame(game);
    }

    fclose(file);
    file = fopen("sequentialFile", "rb");
    Game gameRead = sf_read(file, 730);
    showGame(&gameRead);
    freeGame(&gameRead);

    fclose(database);
    fclose(file);
    free(line);
	return 0;
}