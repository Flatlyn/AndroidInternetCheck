# AndroidInternetCheck
A drop-in Class/AsyncTask for android that will check for a live internet connection (not just a network connection) and then open a class you pass either an error class or your main class depending on whether you are checking if the internet is broken or has returned.

# Usage
This class is very simply to use. Simply include in your Android project and then call from whatever activity you want to check the internet in as so, 

    new InternetCheck(context, "com.example.app.InternetError", true).execute();
    
The class takes three paramters, the current context, the fully qualified name of the activity you want the class to redirect too, and whether you want to redirect on an error with the internet (True) or functioning internet (False). When calling from your main activity the passed class will probably be your error activity. In your error activity you will likely want a recurring call to this class but with the false parameter and you main activity as the passed activity so that it will automatically return to your main activity when the internet connection is back.

For example, if you have a main activity that preforms some online check (say updating an RSS feed) every 30 seconds, you would include this call before you make the calls to your RSS update functions to ensure the internet is live or redirect to an error page if it's not. 
