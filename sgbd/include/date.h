#pragma once

// Objeto de Data
typedef struct {
	int month;
	int year;
} Date;

// Metodo para retorna o mes em string
char *getMonthName(int month);

// Metodo para retornar mes em numero
int getMonthNumber(char *month);