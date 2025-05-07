@echo off
echo Starting servlet reorganization...

rem Create backup directory
mkdir "src\java\gamevaultbase\servlets\backup"

rem Create package directories (in case they don't exist)
mkdir "src\java\gamevaultbase\servlets\public"
mkdir "src\java\gamevaultbase\servlets\user"
mkdir "src\java\gamevaultbase\servlets\admin"
mkdir "src\java\gamevaultbase\servlets\api"

rem Public Servlets
copy "src\java\gamevaultbase\servlets\HomeServlet.java" "src\java\gamevaultbase\servlets\public\"
copy "src\java\gamevaultbase\servlets\LoginServlet.java" "src\java\gamevaultbase\servlets\public\"
copy "src\java\gamevaultbase\servlets\RegisterServlet.java" "src\java\gamevaultbase\servlets\public\"
copy "src\java\gamevaultbase\servlets\BrowseServlet.java" "src\java\gamevaultbase\servlets\public\"
copy "src\java\gamevaultbase\servlets\GameDetailServlet.java" "src\java\gamevaultbase\servlets\public\"

rem User Servlets
copy "src\java\gamevaultbase\servlets\ProfileServlet.java" "src\java\gamevaultbase\servlets\user\"
copy "src\java\gamevaultbase\servlets\EditProfileServlet.java" "src\java\gamevaultbase\servlets\user\"
copy "src\java\gamevaultbase\servlets\CartServlet.java" "src\java\gamevaultbase\servlets\user\"
copy "src\java\gamevaultbase\servlets\RemoveFromCartServlet.java" "src\java\gamevaultbase\servlets\user\"
copy "src\java\gamevaultbase\servlets\PlaceOrderServlet.java" "src\java\gamevaultbase\servlets\user\"
copy "src\java\gamevaultbase\servlets\OrdersServlet.java" "src\java\gamevaultbase\servlets\user\"
copy "src\java\gamevaultbase\servlets\LogoutServlet.java" "src\java\gamevaultbase\servlets\user\"
copy "src\java\gamevaultbase\servlets\ReviewServlet.java" "src\java\gamevaultbase\servlets\user\"

rem Admin Servlets
copy "src\java\gamevaultbase\servlets\GameManagementServlet.java" "src\java\gamevaultbase\servlets\admin\"
copy "src\java\gamevaultbase\servlets\GameImageUploadServlet.java" "src\java\gamevaultbase\servlets\admin\"
copy "src\java\gamevaultbase\servlets\DeleteGameServlet.java" "src\java\gamevaultbase\servlets\admin\"
copy "src\java\gamevaultbase\servlets\DatabaseCheckServlet.java" "src\java\gamevaultbase\servlets\admin\"
copy "src\java\gamevaultbase\servlets\DummyDataServlet.java" "src\java\gamevaultbase\servlets\admin\"
copy "src\java\gamevaultbase\servlets\GameJsonLoaderServlet.java" "src\java\gamevaultbase\servlets\admin\"

rem Move all original files to backup directory
move "src\java\gamevaultbase\servlets\*.java" "src\java\gamevaultbase\servlets\backup\"
rem Move base directory back (if it was moved)
if exist "src\java\gamevaultbase\servlets\backup\base" (
    move "src\java\gamevaultbase\servlets\backup\base" "src\java\gamevaultbase\servlets\"
)

echo Servlet files copied to new package directories.
echo Original files moved to backup directory.
echo.
echo IMPORTANT: You need to update the package declarations in each file!
echo.