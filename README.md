URL_lookup
==========

Basic URL lookup service which doesn’t require authentication to make a query.  


Security Notes

Database Queries are parameterized
Regex check is done on the URL input by the user to prevent injections and to also save time in querying the database if the URL is not a valid URL (to protect from bots)
Credentials are not hardcoded in the source code but put in a config file stored in a separate folder ( access to which should be protected )
Passwords have high entropy
Generic error messages are generated which don’t bleed information about the system or configuration
Very less information about errors is sent to the users - error messages should go to logs to which access should be restricted



