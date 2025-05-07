@echo off
echo Updating package declarations in servlet files...

rem Update Public Servlets
echo Updating public servlets...
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\public\HomeServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.public;' | Set-Content 'src\java\gamevaultbase\servlets\public\HomeServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\public\LoginServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.public;' | Set-Content 'src\java\gamevaultbase\servlets\public\LoginServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\public\RegisterServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.public;' | Set-Content 'src\java\gamevaultbase\servlets\public\RegisterServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\public\BrowseServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.public;' | Set-Content 'src\java\gamevaultbase\servlets\public\BrowseServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\public\GameDetailServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.public;' | Set-Content 'src\java\gamevaultbase\servlets\public\GameDetailServlet.java'"

rem Update User Servlets
echo Updating user servlets...
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\ProfileServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\ProfileServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\EditProfileServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\EditProfileServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\CartServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\CartServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\RemoveFromCartServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\RemoveFromCartServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\PlaceOrderServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\PlaceOrderServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\OrdersServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\OrdersServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\LogoutServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\LogoutServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\user\ReviewServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.user;' | Set-Content 'src\java\gamevaultbase\servlets\user\ReviewServlet.java'"

rem Update Admin Servlets
echo Updating admin servlets...
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\admin\GameManagementServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.admin;' | Set-Content 'src\java\gamevaultbase\servlets\admin\GameManagementServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\admin\GameImageUploadServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.admin;' | Set-Content 'src\java\gamevaultbase\servlets\admin\GameImageUploadServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\admin\DeleteGameServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.admin;' | Set-Content 'src\java\gamevaultbase\servlets\admin\DeleteGameServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\admin\DatabaseCheckServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.admin;' | Set-Content 'src\java\gamevaultbase\servlets\admin\DatabaseCheckServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\admin\DummyDataServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.admin;' | Set-Content 'src\java\gamevaultbase\servlets\admin\DummyDataServlet.java'"
powershell -Command "(Get-Content 'src\java\gamevaultbase\servlets\admin\GameJsonLoaderServlet.java') -replace 'package gamevaultbase.servlets;', 'package gamevaultbase.servlets.admin;' | Set-Content 'src\java\gamevaultbase\servlets\admin\GameJsonLoaderServlet.java'"

echo Package declarations updated successfully.
echo.
echo IMPORTANT: You need to update the web.xml file to reflect the new package structure!
echo.