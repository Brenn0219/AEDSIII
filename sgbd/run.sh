clear
mkdir build
cd build
cmake .. -G "MinGW Makefiles" -D CMAKE_C_COMPILER=gcc
cmake --build .
./sgbd.exe
cd ..