# Caesar Cipher Request

Solution made for a code challenge. The app performs a GET request to an API that returns a JSON with a caesar-ciphered text and the number of letters displacement which was used for encoding. This JSON is saved as a file which will be used by the app. Then, the algorithm decodes the text, creates a SHA1 encoded hash over the decoded text and save both in JSON file. Finally, The JSON file is submitted over HTTP POST request to the API. The response is the score or an error.

Final score: 100.
