cmake_minimum_required(VERSION 3.27.2)

# set( CMAKE_CXX_COMPILER "C:/MinGW/bin/g++" )
# set( CMAKE_C_COMPILER "C:/MinGW/bin/gcc" )

project(sgbd C)

add_subdirectory(src)
add_subdirectory(include)

add_executable(${PROJECT_NAME} src/main.c)

target_link_libraries(${PROJECT_NAME} PRIVATE source)