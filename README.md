# redditclient
This is a simple client for browsing reddit/top.

Client loads 50 first entries from reddit/top, shows title and a thumbnail. When scrolled to the bottom of the list, loads 50 more entries.
By clicking on entry, you can browse it in a custom chrome tab.

Supports reconnect feature.
In case of getting an error during loading entries, you will see a screen with error message. You can blame me and restart the app.

Project is implemented in kotlin using MVP architecture.
Calls to reddit api are made with the help of retrofit2, results are aquired via RxJava.
