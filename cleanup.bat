@echo off
echo Cleaning up redundant files...

rem Remove backup servlet files
rmdir /s /q "src\java\gamevaultbase\servlets\backup"

rem Remove the 'public' package directory (since we renamed it to 'pub')
rmdir /s /q "src\java\gamevaultbase\servlets\public"

rem Remove temporary batch files
del "move_servlets.bat"
del "update_packages.bat"
del "rename_public_package.bat"
del "update_webxml.bat"

rem Remove web.xml backup (optional - leave this commented if you want to keep it)
rem del "web\WEB-INF\web.xml.bak"

echo Cleanup completed successfully.
echo.