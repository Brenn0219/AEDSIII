#pragma once // importacao de bibliotecas que os .c nao precisaram importar novamente

#include <stdbool.h>
#include "date.h"

// Objeto de Game
typedef struct {
	int appId, age, dlcs, avgPt, sizeLanguages, sizeGeneros;
	char *name, *owners, *website, *developers;
	float price, upvotes;
	bool windows, mac, linxs;
	Date date;
	char **languages, **generos;
} Game;

// Metodo para remover os caracteres
void removeAll(char *string, char swapped);

// Metodo para retornar o total de elementos de um array (string)
int sizeArray(char *attribute);

// Metodo para calcular a Porcentagem do Upvotes
double calculatePercentage(char *upvotes, char *notUpvotes);

// Metodo para converter string para date
void convertDate(Game *game, char *date);

// Metodo que retorna uma string/atributo
char* separateAttributes(char* line, int *position);

// Metodo para formatar os arrays
char** formartArray(char *line, int *n);

// Metodo de ler/criar um jogo
void toRead(Game *game, char *line);

// Metodo para clonar jogo
Game* clone(Game *game);

// Metodo para liberar o game
void freeGame(Game *game);