# share.ly
An android app, shortening uris from the share action.

# Note for building

If you want to build it you have to provide you own
bit.ly access key in the file

 app/src/main/res/values/keys.xml

containing something like
 
 <?xml version="1.0" encoding="utf-8"?>
 <resources>
    <string name="bitly_access_key">…</string>
 </resources>

. At a later point in time, there will be a login
functionality but not right now, I just wanted to
keep it simple.
