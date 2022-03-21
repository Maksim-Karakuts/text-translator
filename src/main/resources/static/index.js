function translate() {
    let errorDiv = $('.error');
    let initialTextValue = $('.initialText').val();

    errorDiv.empty();
    if (initialTextValue !== '') {
        let initialText = {};
        initialText['initialLanguage'] = 'en';
        initialText['targetLanguage'] = 'ru';
        initialText['text'] = initialTextValue;
        $.ajax({
            url: 'translation',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(initialText),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                $('.translatedText').text(data.translatedText);
            },
            error: function (jqXhr) {
                errorDiv.text(JSON.parse(jqXhr.responseText).message);
            }
        });
    } else {
        errorDiv.text("Please, enter text to translate");
    }
}

$(document).ready(function () {
    $('.translateButton').click(function () {
        translate();
    });
});