@echo off
echo Running migrations and seeders...
php artisan migrate:fresh --seed
echo.
echo Linking storage...
php artisan storage:link
echo.
pause
