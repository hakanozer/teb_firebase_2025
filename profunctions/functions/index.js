const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.exampleFunction = functions.https.onCall((data, context) => {
    const userId = data.userId;
    const action = data.action;

    // Burada Firebase Firestore ya da diğer veritabanı işlemlerini kullanabilirsiniz.
    return { response: `Kullanıcı ID: ${userId}, Yapılan işlem: ${action}` };
});