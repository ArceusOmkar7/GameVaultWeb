@echo off
echo Running database tests for GameVault...
echo.

echo Compiling test classes...
cd d:\University\Sem 4\Projects\Java\GameVaultWebUpdated
javac -cp "build/web/WEB-INF/classes;lib/*" -d build/web/WEB-INF/classes src/java/gamevaultbase/TestDirectDatabase.java
javac -cp "build/web/WEB-INF/classes;lib/*" -d build/web/WEB-INF/classes src/java/gamevaultbase/TestAddGame.java

echo.
echo Running direct database test...
java -cp "build/web/WEB-INF/classes;lib/*" gamevaultbase.TestDirectDatabase

echo.
echo Running game storage test...
java -cp "build/web/WEB-INF/classes;lib/*" gamevaultbase.TestAddGame

echo.
echo Tests completed.
pause
