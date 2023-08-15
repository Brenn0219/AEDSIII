#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "game.h"
#include "gameprinting.h"

int gameSizeInByte(Game *game) {
    int counter = sizeof(bool);

    counter += 2 * sizeof(int) + strlen(game->name); 

    return counter;
}

void writeGameToFile(FILE *file, Game *game) {
    int n;
    bool status = true;
    
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

long sf_create(FILE *file, Game *game) {
    long position;

    fseek(file, 0, SEEK_END);
    position = ftell(file);
    writeGameToFile(file, game);

    return position;
}

void sf_showFile(FILE *file) { 
    Game game;
    int n;
    bool status;
    
    rewind(file);
    fread(&status, sizeof(bool), 1, file);
    fread(&game.appId, sizeof(int), 1, file);
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
    game.languages = malloc(sizeof(char *) * (game.sizeLanguages + 1));
    for(int i = 0; i < game.sizeLanguages; i++) {
        fread(&n, sizeof(int), 1, file); // size languages[i]
        fread(&game.languages[i], n, 1, file); // languages[i]
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
    game.generos = malloc(sizeof(char *) * (game.sizeLanguages + 1));
    for(int i = 0; i < game.sizeGeneros; i++) {
        fread(&n, sizeof(int), 1, file); // size generos[i]
        fread(&game.generos[i], n, 1, file); // generos[i]
    }

    showGame(&game);
}