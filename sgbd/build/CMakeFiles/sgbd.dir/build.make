# CMAKE generated file: DO NOT EDIT!
# Generated by "MinGW Makefiles" Generator, CMake Version 3.27

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "C:\Program Files\CMake\bin\cmake.exe"

# The command to remove a file.
RM = "C:\Program Files\CMake\bin\cmake.exe" -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = C:\Users\brenno\Documents\AEDSIII\sgbd

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = C:\Users\brenno\Documents\AEDSIII\sgbd\build

# Include any dependencies generated for this target.
include CMakeFiles/sgbd.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/sgbd.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/sgbd.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/sgbd.dir/flags.make

CMakeFiles/sgbd.dir/src/main.c.obj: CMakeFiles/sgbd.dir/flags.make
CMakeFiles/sgbd.dir/src/main.c.obj: CMakeFiles/sgbd.dir/includes_C.rsp
CMakeFiles/sgbd.dir/src/main.c.obj: C:/Users/brenno/Documents/AEDSIII/sgbd/src/main.c
CMakeFiles/sgbd.dir/src/main.c.obj: CMakeFiles/sgbd.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir=C:\Users\brenno\Documents\AEDSIII\sgbd\build\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/sgbd.dir/src/main.c.obj"
	C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/sgbd.dir/src/main.c.obj -MF CMakeFiles\sgbd.dir\src\main.c.obj.d -o CMakeFiles\sgbd.dir\src\main.c.obj -c C:\Users\brenno\Documents\AEDSIII\sgbd\src\main.c

CMakeFiles/sgbd.dir/src/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/sgbd.dir/src/main.c.i"
	C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E C:\Users\brenno\Documents\AEDSIII\sgbd\src\main.c > CMakeFiles\sgbd.dir\src\main.c.i

CMakeFiles/sgbd.dir/src/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/sgbd.dir/src/main.c.s"
	C:\MinGW\bin\gcc.exe $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S C:\Users\brenno\Documents\AEDSIII\sgbd\src\main.c -o CMakeFiles\sgbd.dir\src\main.c.s

# Object files for target sgbd
sgbd_OBJECTS = \
"CMakeFiles/sgbd.dir/src/main.c.obj"

# External object files for target sgbd
sgbd_EXTERNAL_OBJECTS =

sgbd.exe: CMakeFiles/sgbd.dir/src/main.c.obj
sgbd.exe: CMakeFiles/sgbd.dir/build.make
sgbd.exe: src/libsource.a
sgbd.exe: CMakeFiles/sgbd.dir/linkLibs.rsp
sgbd.exe: CMakeFiles/sgbd.dir/objects1.rsp
sgbd.exe: CMakeFiles/sgbd.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --bold --progress-dir=C:\Users\brenno\Documents\AEDSIII\sgbd\build\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable sgbd.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles\sgbd.dir\link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/sgbd.dir/build: sgbd.exe
.PHONY : CMakeFiles/sgbd.dir/build

CMakeFiles/sgbd.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles\sgbd.dir\cmake_clean.cmake
.PHONY : CMakeFiles/sgbd.dir/clean

CMakeFiles/sgbd.dir/depend:
	$(CMAKE_COMMAND) -E cmake_depends "MinGW Makefiles" C:\Users\brenno\Documents\AEDSIII\sgbd C:\Users\brenno\Documents\AEDSIII\sgbd C:\Users\brenno\Documents\AEDSIII\sgbd\build C:\Users\brenno\Documents\AEDSIII\sgbd\build C:\Users\brenno\Documents\AEDSIII\sgbd\build\CMakeFiles\sgbd.dir\DependInfo.cmake "--color=$(COLOR)"
.PHONY : CMakeFiles/sgbd.dir/depend
