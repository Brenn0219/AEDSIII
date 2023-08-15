project(c)

add_library(source
    app/date.c
    app/game.c
    app/gameprinting.c
    app/sequentialfile.c
    tmp/games.csv
)

target_include_directories(source
    PUBLIC
    ${CMAKE_CURRENT_SOURCE_DIR}/../include
)