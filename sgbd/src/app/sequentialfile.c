#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "game.h"
#include "gameprinting.h"

int sf_gameSizeInByte(Game *game) {
    int counter;

    counter = (4 * sizeof(bool)) + (12 * sizeof(int)) + (2 * sizeof(double)) + strlen(game->name) + strlen(game->owners) + strlen(game->website) + strlen(game->developers); 

    for(int i = 0; i < game->sizeLanguages; i++) {
        counter += sizeof(int) + strlen(game->languages[i]);
    }

    for(int i = 0; i < game->sizeGeneros; i++) {
        counter += sizeof(int) + strlen(game->generos[i]);
    }

    return counter;
}

void sf_writeGameToFile(FILE *file, Game *game) {
    int n, recordSize = sf_gameSizeInByte(game);
    bool status = true;
    
    fwrite(&recordSize, sizeof(int), 1, file); // recordSize
    fwrite(&status, sizeof(bool), 1, file); // status 
    fwrite(&game->appId, sizeof(int), 1, file); // id
    n = strlen(game->name); 
    fwrite(&n, sizeof(int), 1, file); // size name 
    fwrite(&game->name, n, 1, file); // name
    fwrite(&game->date.month, sizeof(int), 1, file); // date month
    fwrite(&game->date.year, sizeof(int), 1, file); // date year
    n = strlen(game->owners); 
    fwrite(&n, sizeof(int), 1, file); // size owners 
    fwrite(&game->owners, n, 1, file); // owners
    fwrite(&game->age, sizeof(int), 1, file); // age
    fwrite(&game->price, sizeof(double), 1, file); // price
    fwrite(&game->dlcs, sizeof(int), 1, file); // dlcs

    fwrite(&game->sizeLanguages, sizeof(int), 1, file); // total elements in languages(array)
    for(int i = 0; i < game->sizeLanguages; i++) {
        n = strlen(game->languages[i]);
        fwrite(&n, sizeof(int), 1, file);
        fwrite(&game->languages[i], n, 1, file);
    }

    n = strlen(game->website); 
    fwrite(&n, sizeof(int), 1, file); // size website 
    fwrite(&game->website, n, 1, file); // website
    fwrite(&game->windows, sizeof(bool), 1, file); // windows
    fwrite(&game->linxs, sizeof(bool), 1, file); // linxs   
    fwrite(&game->mac, sizeof(bool), 1, file); // mac
    fwrite(&game->upvotes, sizeof(double), 1, file); // upvotes
    fwrite(&game->avgPt, sizeof(int), 1, file); // avgPt
    n = strlen(game->developers); 
    fwrite(&n, sizeof(int), 1, file); // size developers 
    fwrite(&game->developers, n, 1, file); // developers

    fwrite(&game->sizeGeneros, sizeof(int), 1, file); // total elements in generos(array)
    for(int i = 0; i < game->sizeGeneros; i++) {
        n = strlen(game->generos[i]); 
        fwrite(&n, sizeof(int), 1, file); // size generos[i]
        fwrite(&game->generos[i], n, 1, file); // generos[i]
    }
}

Game sf_readBytesForGame(FILE *file) {
    Game game;
    int n;

    fread(&game.appId, sizeof(int), 1, file); // id
    fread(&n, sizeof(int), 1, file); // size name 
    fread(&game.name, n, 1, file); // name
    fread(&game.date.month, sizeof(int), 1, file); // date month
    fread(&game.date.year, sizeof(int), 1, file); // date year 
    fread(&n, sizeof(int), 1, file); // size owners 
    fread(&game.owners, n, 1, file); // owners
    fread(&game.age, sizeof(int), 1, file); // age
    fread(&game.price, sizeof(double), 1, file); // price
    fread(&game.dlcs, sizeof(int), 1, file); // dlcs

    fread(&game.sizeLanguages, sizeof(int), 1, file); // total elements in languages(array)

    printf("%d %s %s/%d %s %d %.2f %d [", game.appId, game.name, getMonthName(game.date.month), game.date.year, game.owners, game.age, game.price, game.dlcs);

    game.languages = malloc(sizeof(char *) * (game.sizeLanguages + 1));
    for(int i = 0; i < game.sizeLanguages; i++) {
        fread(&n, sizeof(int), 1, file); // size languages[i]
        fread(&game.languages[i], n, 1, file); // languages[i]

        printf("%s, ", game.languages[i]);
    }

    fread(&n, sizeof(int), 1, file); // size website 
    fread(&game.website, n, 1, file); // website
    fread(&game.windows, sizeof(bool), 1, file); // windows
    fread(&game.linxs, sizeof(bool), 1, file); // linxs   
    fread(&game.mac, sizeof(bool), 1, file); // mac
    fread(&game.upvotes, sizeof(double), 1, file); // upvotes
    fread(&game.avgPt, sizeof(int), 1, file); // avgPt
    fread(&n, sizeof(int), 1, file); // size developers 
    fread(&game.developers, n, 1, file); // developers
    
    fread(&game.sizeGeneros, sizeof(int), 1, file); // total elements in languages(array)

    printf("] %s %s %s %s %.2f %d %s [", game.website, (game.windows ? "true" : "false"), (game.linxs ? "true" : "false"), (game.mac ? "true" : "false"), game.upvotes, game.avgPt, game.developers);

    game.generos = malloc(sizeof(char *) * (game.sizeGeneros + 1));
    for(int i = 0; i < game.sizeGeneros; i++) {
        fread(&n, sizeof(int), 1, file); // size generos[i]
        fread(&game.generos[i], n, 1, file); // generos[i]

        printf("%s, ", game.generos[i]);
    }

    printf("]\n");

    return game;
}

long sf_create(FILE *file, Game *game) {
    long position;

    fseek(file, 0, SEEK_END);
    position = ftell(file);
    sf_writeGameToFile(file, game);

    return position;
}

Game sf_read(FILE *file, int x) {
    int recordSize, id;
    bool status;
    long position;
    Game game;

    rewind(file);
    printf("begnning position: %ld\n", ftell(file));
    while (!feof(file)) {
        fread(&recordSize, sizeof(int), 1, file);
        fread(&status, sizeof(bool), 1, file);

        if(status) {
            fread(&id, sizeof(int), 1, file);

            if(x == id) {
                printf("before position: %ld\n", ftell(file));
                fseek(file, -sizeof(int), SEEK_CUR);
                printf("after position: %ld\n", ftell(file));
                game = sf_readBytesForGame(file);
                break;
            } else {
                fseek(file, (recordSize - (2 * sizeof(int) + sizeof(bool))), SEEK_CUR);
            }
        } else {
            fseek(file, (recordSize - (sizeof(int) + sizeof(bool))), SEEK_CUR);
        }
    }
    
    return game;
}

void sf_showFile(FILE *file) { 
    Game game;
    int n, recordSize;
    bool status;
    
    // rewind(file);
    fseek(file, 0, SEEK_SET);
    while(!feof(file)) {
        fread(&recordSize, sizeof(int), 1, file);
        fread(&status, sizeof(bool), 1, file);
        game = sf_readBytesForGame(file);
        showGame(&game);
        freeGame(&game);
    }
}