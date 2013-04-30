ChatPortlet
=================================
ChatPortlet is portlet for IM.  It uses PortletBridge 3.2.0.Final, Richfaces 4.3.1Final, Smack (for XMPP), PircBotX (for IRC)

Features:
------------------
- XMPP support(GTalk and Facebook chat settings are pre-defined)
- presence and text status change support (EDIT,DND, VIEW)
- IRC support
- ajax-based communication
- multiple accounts support
- storing user accounts into portlet preferences (Need to use a user's dashboard for user-specific preferences)
- storing history



ChatPortlet shows what is possible with PortletBridge in GateIn portal. It's a technologic demo, therefore some bugs and issues may show up. It was tested with GTalk and Facebook (It has some issues with presence) accounts.
If you find some bugs (and I'm really sure you will ;) ) leave comment or email me to 5mattho@gmail.com



User Guide
----------- 
1. Connecting  
 You need to fill all nessesary fields for connecting. If "Save account" is checked, settings are stored into portlet preferences

2. Communication
For instant messaging pick contact from contact list and new converasation is opened. Conversations are saved automatically into database.

3. Presence change
You can change presence and text status. After typing your own status, it has to be confirmed by selecting presence mode.

4. Accounts managing 
For managing connected and stored accounts click on "+" (Add) button. 

5. Folder refresh 
For current folder refresh click on refresh button.






