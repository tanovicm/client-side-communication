# client-side-communication 

Application based on socket communication which receives messages from server, decodes, processes and responds back. On application termination, all unprocessed messages are saved to the noSQL database. On application start, all unprocessed messages from the database are loaded and processed. All application processes are done in multithreaded way. Written in Java using Maven and Eclipse.
