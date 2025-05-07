@echo off
echo Updating web.xml servlet class paths...

rem Create a backup of web.xml
copy "web\WEB-INF\web.xml" "web\WEB-INF\web.xml.bak"

rem Update Public (now pub) Servlets
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.HomeServlet', 'gamevaultbase.servlets.pub.HomeServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.LoginServlet', 'gamevaultbase.servlets.pub.LoginServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.RegisterServlet', 'gamevaultbase.servlets.pub.RegisterServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.BrowseServlet', 'gamevaultbase.servlets.pub.BrowseServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.GameDetailServlet', 'gamevaultbase.servlets.pub.GameDetailServlet' | Set-Content 'web\WEB-INF\web.xml'"

rem Update User Servlets
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.LogoutServlet', 'gamevaultbase.servlets.user.LogoutServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.ProfileServlet', 'gamevaultbase.servlets.user.ProfileServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.EditProfileServlet', 'gamevaultbase.servlets.user.EditProfileServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.CartServlet', 'gamevaultbase.servlets.user.CartServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.RemoveFromCartServlet', 'gamevaultbase.servlets.user.RemoveFromCartServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.PlaceOrderServlet', 'gamevaultbase.servlets.user.PlaceOrderServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.OrdersServlet', 'gamevaultbase.servlets.user.OrdersServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.ReviewServlet', 'gamevaultbase.servlets.user.ReviewServlet' | Set-Content 'web\WEB-INF\web.xml'"

rem Update Admin Servlets
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.GameManagementServlet', 'gamevaultbase.servlets.admin.GameManagementServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.GameImageUploadServlet', 'gamevaultbase.servlets.admin.GameImageUploadServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.DeleteGameServlet', 'gamevaultbase.servlets.admin.DeleteGameServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.DatabaseCheckServlet', 'gamevaultbase.servlets.admin.DatabaseCheckServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.DummyDataServlet', 'gamevaultbase.servlets.admin.DummyDataServlet' | Set-Content 'web\WEB-INF\web.xml'"
powershell -Command "(Get-Content 'web\WEB-INF\web.xml') -replace 'gamevaultbase.servlets.GameJsonLoaderServlet', 'gamevaultbase.servlets.admin.GameJsonLoaderServlet' | Set-Content 'web\WEB-INF\web.xml'"

echo web.xml servlet class paths updated successfully.
echo.
echo A backup of the original web.xml has been created as web.xml.bak
echo.