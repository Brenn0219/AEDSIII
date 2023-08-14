#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "game.h"

const int SIZE_ATTRIBUTE = 385;

// Metodo para remover os caracteres
void removeAll(char *string, char swapped) {
    int counter = 0, n = strlen(string);
    char aux[SIZE_ATTRIBUTE];

    for(int i = 0; i < n; i++) {
        if(string[i] != swapped) {
            aux[counter++] = string[i];
        }
    }

    aux[counter] = '\0';
    strcpy(string, aux);
}

// Metodo para retornar o total de elementos de um array (string)
int sizeArray(char *attribute) {
    int counter = 1, n = strlen(attribute);

    for(int i = 0; i < n; i++) {
        if(attribute[i] == ',') {
            counter++;
        }
    }

    return counter;
}

// Metodo para calcular a Porcentagem do Upvotes
double calculatePercentage(char *upvotes, char *notUpvotes) {
    return (atof(upvotes) / (atof(upvotes) + atof(notUpvotes)));
}

// Metodo para converter string para date
void convertDate(Game *game, char *date) {
    char month[4], year[5];
    int n = strlen(date), i = 0, counter = 0;

    for(; i < 3; i++) {
        month[counter++] = date[i];
    }
    month[counter] = '\0';

    switch (n) {
        case 12:
            i = 8;
            break;
        case 11:
            i = 7;
            break;
        case 8:
            i = 4;
            break;
    }

    counter = 0;
    for(; i < n; i++) {
        year[counter++] = date[i];
    }

    game->date.month = getMonthNumber(month);
    game->date.year = atoi(year);
}

// Metodo que retorna uma string/atributo
char* separateAttributes(char* line, int *position) {
    int index = *position, counter = 0, n = strlen(line);
    char attributes[SIZE_ATTRIBUTE];
    char separator = ',';

    if(line[index] == '"') {
        separator = '"';
        index++;
    }

    if(line[index] == ' ') {
        index++;
    }

    while(index < n && line[index] != separator) {
        attributes[counter++] = line[index++];
    }

    index++;
    if(separator == '"') {
        index++;
    } 
    
    attributes[counter] = '\0';
    char *aux = malloc(sizeof(char) * (strlen(attributes) + 1));
    strcpy(aux, attributes);
    
    *position = index;

    return aux;
}

// Metodo para formatar os arrays
char** formartArray(char *line, int *n) {
    int j = 0; 

    removeAll(line, '\'');
    removeAll(line, '[');
    removeAll(line, ']');
    
    *n = sizeArray(line); 
    char **array = malloc(sizeof(char*) * (*n));

    for(int i = 0; i < *n; i++) {
        array[i] = separateAttributes(line, &j);
    }

    if(*n == 1) {
        int n = strlen(array[0]);
        char aux[n];
        strcpy(aux, array[0]);

        for(int i = 0; i < n; i++) {
            if(aux[i] == '\n' || aux[i] == '\r') {
                aux[i] = '\0';
            }
        }

        strcpy(array[0], aux);
    }

    return array;
}

// Metodo de ler/criar um jogo
void toRead(Game *game, char *line) {
	int i = 0;
	char *attributes;

	// appId
    attributes = separateAttributes(line, &i);
    game->appId = atoi(attributes);
    free(attributes);

    // name 
    game->name = separateAttributes(line, &i);

    // date
    attributes = separateAttributes(line, &i);
    convertDate(game, attributes);
    free(attributes);

    // owners
    game->owners = separateAttributes(line, &i);

    // age
    attributes = separateAttributes(line, &i);
    game->age = atoi(attributes);
    free(attributes);

    // price
    attributes = separateAttributes(line, &i);
    game->price = atof(attributes);
    free(attributes);

    // dlcs
    attributes = separateAttributes(line, &i);
    game->dlcs = atof(attributes);
    free(attributes);

    // languages
    attributes = separateAttributes(line, &i);
    game->sizeLanguages = 0;
    game->languages = formartArray(attributes, &game->sizeLanguages);
    free(attributes);

    // website
    game->website = separateAttributes(line, &i);

    // windows
    attributes = separateAttributes(line, &i);
    game->windows = (strcmp(attributes, "True") ? false:true);
    free(attributes);

    // linuxs
    attributes = separateAttributes(line, &i);
    game->linxs = (strcmp(attributes, "True") ? false:true);
    free(attributes);

    // mac
    attributes = separateAttributes(line, &i);
    game->mac = (strcmp(attributes, "True") ? false:true);
    free(attributes);

    // upvotes
    char *upvotes = separateAttributes(line, &i);
    char *notupvotes = separateAttributes(line, &i);
    game->upvotes = calculatePercentage(upvotes, notupvotes);
    free(upvotes);
    free(notupvotes);

    // avgPt
    attributes = separateAttributes(line, &i);
    game->avgPt = atoi(attributes); 
    free(attributes);

    // developers
    game->developers = separateAttributes(line, &i);

    // generos
    attributes = separateAttributes(line, &i);
    game->sizeGeneros = 0;
    if(strlen(attributes)) {
        game->generos = formartArray(attributes, &game->sizeGeneros);
    }
    free(attributes); 
}

// Metodo para clonar jogo
Game* clone(Game *game) {
    Game *cloned = malloc(sizeof(game));

    // appId
    cloned->appId = game->appId;
    
    // name
    strcpy(cloned->name, game->name);

    // date
    cloned->date.month = game->date.month;
    cloned->date.year = game->date.year;
    
    // owners
    strcpy(cloned->owners, game->owners);
    
    // age
    cloned->age = game->age;
    
    // price
    cloned->price = game->price;
    
    // dlcs
    cloned->dlcs = game->dlcs;
    
    // languages
    cloned->sizeLanguages = game->sizeLanguages;
    for(int i = 0; i < cloned->sizeLanguages; i++) {
        strcpy(cloned->languages[i], game->languages[i]);
    }

    // website
    strcpy(cloned->website, game->website);
    
    // windows
    cloned->windows = game->windows;
    
    // linxs
    cloned->linxs = game->linxs;
    
    // mac
    cloned->mac = game->mac;
    
    // upvotes
    cloned->upvotes = game->upvotes;
    
    // abgPt
    cloned->avgPt = game->avgPt;
    
    // developers
    strcpy(cloned->developers, game->developers);
    
    // generos
    cloned->sizeGeneros = game->sizeGeneros;
    for(int i = 0; i < cloned->sizeGeneros; i++) {
        strcpy(cloned->generos[i], game->generos[i]);
    }

    return cloned;
}

// Metodo para liberar o game
void freeGame(Game *game) {
    free(game->name);
    free(game->owners);
    for(int i = 0; i < game->sizeLanguages; i++) {
        free(game->languages[i]);
    }
    free(game->languages);
    free(game->website);
    free(game->developers);
    if(game->sizeGeneros > 0) {
        for(int i = 0; i < game->sizeGeneros; i++) {
            free(game->generos[i]);
        }
        free(game->generos);
    }
    free(game);
}