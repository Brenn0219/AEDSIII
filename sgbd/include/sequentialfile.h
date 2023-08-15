#pragma once

#include <stdio.h>
#include "game.h"

int gameSizeInByte(Game *game);
void writeGameToFile(FILE *file, Game *game);
long sf_create(FILE *file, Game *game);
void sf_showFile(FILE *file);