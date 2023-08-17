#pragma once

#include <stdio.h>
#include "game.h"

int sf_gameSizeInByte(Game *game);
void sf_writeGameToFile(FILE *file, Game *game);
Game sf_readBytesForGame(FILE *file);
long sf_create(FILE *file, Game *game);
Game sf_read(FILE *file, int x);
void sf_showFile(FILE *file);