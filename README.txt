GITHUB URL:https://github.com/NicholasPierce1/SmartShopper.git
Matthew Berry used the following thread to toggle visibility of widgets
https://stackoverflow.com/questions/5093466/visible-and-invisible-with-a-checkbox
IF the app crashes(shouldn't) it's advisable to reset the entire app
VERSION:
Pleasee use 8.1 or higher

ADMIN LOGIN INFORMATION:
id: b
pw: b

Consumer Side (Select "Start Searching" on initial view)
- items to search: chips, candy, apples (should not yield any results- though subject to change)
- recycle view appears, select desired item

Admin side:
- ADMIN LOGIN INFORMATION: username: 'b', pw: 'b' (owner-- has highest permissions)
-  ADMIN LOGIN INFORMATION: username: 'imTheAdmin', pw: 'adminadmin' (managing store admin -- middle level permissions)
- pre-created barcodes that exist to store: 220991, 220994, 111111
- barcodes that exist but not to store: 110088
- admin empi id that exist to store: 321, 999 (none of the two admins above have these emp id so feel free to modify/delete as needed)

Functionality and Completion Status:
- Allow users to select stores and declare user role/identity (done)
- Allow consumers to query and see where items reside at(done)
- Allow consumers to navigate freely on consumer side via “emulated” tab bar (done)
- Allow admin to login and see profile state/attributes (done)
- Allow admin to create, update, and delete admin with respect to input validation (done)
- Allow admin to create, update, and delete items  with respect to input validation (done)
- Allow admin to freely navigate internally in admin panel (done)
- Responsive design implementation on all views (done)
