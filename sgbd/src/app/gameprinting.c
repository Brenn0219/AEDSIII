#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "gameprinting.h"
#include "game.h"


// Metodo para imprimir Horas e os Minutos corretamente
void formatHours(int seconds) {
    int hour = seconds / 60;
    int min = seconds % 60;

    if(hour == 0 && min == 0) {
        printf("null ");
    } else if (hour > 0 && min > 0) {
        printf("%dh %dm ", hour, min);
    } else {
        if(hour > 0) {
            printf("%dh ", hour);
        }
        if(min > 0) {
            printf("%dm ", min);
        }   
    }
}

// Metodo para Arredondar a Porcentagem
int formatPercentage(double conta) {
    return ((int)round(conta * 100));
}

// Metodo de imprimir
void show(Game *game) {
    printf("%d %s %s/%d %s %d %.02f %d [", game->appId, game->name, getMonthName(game->date.month), game->date.year, game->owners, game->age, game->price, game->dlcs);
    for(int i = 0; i < game->sizeLanguages; i++) {
        if(i == game->sizeLanguages-1) {
            printf("%s] ", game->languages[i]);
        } else {
            printf("%s, ", game->languages[i]);
        }
    }
    printf("%s %s %s %s %d%% ", (strlen(game->website) ? game->website : "null"), (game->windows ? "true":"false"), (game->mac ? "true":"false"), (game->linxs ? "true":"false"), formatPercentage(game->upvotes));
    formatHours(game->avgPt);
    printf("%s ", game->developers);
    if(game->sizeGeneros > 0) {
        printf("[");
        for(int i = 0; i < game->sizeGeneros; i++) {
            if(i == game->sizeGeneros-1) {
                printf("%s]\n", game->generos[i]);
            } else {
                printf("%s, ", game->generos[i]);
            }
        }
    } else {
        printf("null\n");
    }
}