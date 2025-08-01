//=====CURRENT FEATURES=====//
LOGIN USING STUDENT ID AND PASSWORD
SHOW PASSWORD CHECKBOX
REMEMBER ME CHECKBOX
SESSION ID's and COOKIES for remember me
TOAST MESSAGE (SWAL2)

//=====================================================//
See application.properties to reconfigure the database
   - Change the username and password based on your username and password
     
TO USE THE DATABASE, DO THE FOLLOWING:

CREATE A DATABASE (<db_name>)

LOCATE THE PUBLIC INSIDE THE SCHEMAS THEN DELETE IT

RIGHT CLICK ON THE DATABASE THEN SELECT RESTORE, IF YOU CAN'T SEE THE RESTORE, SOMETIMES IT IS INSIDE THE TOOLS

CHOOSE THE (dump-gradely-202508020022.sql) TO REUSE IT

DONE.

//===== TO USE THIS REPOSITORY =====//
OPEN THE TERMINAL IN VS CODE, MAKE SURE THAT YOU HAVE DOWNLOADED GIT, IF NOT GO TO THIS LINK (https://git-scm.com/downloads/win) AND SELECT THE Git for Windows/x64 Setup.

PASTE THIS IN THE GIT TERMINAL:
git clone https://github.com/RalphGervacio/Gradely.git
(This will download the full project into a new folder named Gradely.)

OPEN IT AND BUILD THE PROJECT.

//=====================================================//
"THIS PROJECT IS USING SPRING BOOT + THYMELEAF"

Use @GetMapping for displaying the HTML file, and always use MAV (Model and View) when displaying it.
(See the examples on the existing controllers on how to display the html using the MAV and GET method)

//=====================================================//
NOTE: 
THIS IS ONLY A PROTOTYPE VERSION!

THE DESIGNS AND LOGIC OF THIS IS PURELY FOR PRESENTATION

//===== I can also give you a snippet for the sign up with email verification using SMTP, and do not worry, because it's free, it's just a dependency you have to add on your pom.xml =====//

- Jr. Java Developer (Ralph Gervacio)
