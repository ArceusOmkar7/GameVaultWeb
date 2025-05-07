@echo off
echo Renaming 'public' package to 'pub' to avoid keyword conflicts...

rem Move files from public to pub directory
copy "src\java\gamevaultbase\servlets\public\*.java" "src\java\gamevaultbase\servlets\pub\"

rem Update package declarations in the files
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\pub\HomeServlet.java') -replace 'package gamevaultbase.servlets.public;', 'package gamevaultbase.servlets.pub;' | Set-Content 'src\java\gamevaultbase\servlets\pub\HomeServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\pub\LoginServlet.java') -replace 'package gamevaultbase.servlets.public;', 'package gamevaultbase.servlets.pub;' | Set-Content 'src\java\gamevaultbase\servlets\pub\LoginServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\pub\RegisterServlet.java') -replace 'package gamevaultbase.servlets.public;', 'package gamevaultbase.servlets.pub;' | Set-Content 'src\java\gamevaultbase\servlets\pub\RegisterServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\pub\BrowseServlet.java') -replace 'package gamevaultbase.servlets.public;', 'package gamevaultbase.servlets.pub;' | Set-Content 'src\java\gamevaultbase\servlets\pub\BrowseServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\pub\GameDetailServlet.java') -replace 'package gamevaultbase.servlets.public;', 'package gamevaultbase.servlets.pub;' | Set-Content 'src\java\gamevaultbase\servlets\pub\GameDetailServlet.java'"

rem Update web.xml with new package references
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.public.', 'gamevaultbase.servlets.pub.' | Set-Content 'web\WEB-INF\web.xml'"

echo Package renamed from 'public' to 'pub' successfully.
echo.