URL_lookup
==========

Basic URL lookup service which doesn’t require authentication to make a query.  


Security Notes

1. Database Queries are parameterized to prevent SQL Injections
2. Regex check is done on the URL input to prevent various injections and to also save time in querying the database if the URL is not a valid URL (to protect from bots)
3. Credentials are not hardcoded in the source code but put in a config file stored in a separate folder ( access to which should be protected )
4. Passwords have high entropy
5. Generic error messages are generated which don’t bleed information about the system or configuration which an attacker can leverage to attack the system
6. Very less information about errors is sent to the users - error messages should go to logs to which access should be restricted



