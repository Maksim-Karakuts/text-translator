function translate() {
    let errorDiv = $('.error');
    let initialTextValue = $('.initialText').val();
    let initialLanguageValue = $('.initialLanguage').val();
    let targetLanguageValue = $('.targetLanguage').val();

    errorDiv.empty();
    if (initialTextValue !== '') {
        let initialText = {};
        initialText['initialLanguage'] = initialLanguageValue;
        initialText['targetLanguage'] = targetLanguageValue;
        initialText['text'] = initialTextValue;
        $.ajax({
            url: 'translation',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(initialText),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                $('.translatedText').val(data.translatedText);
            },
            error: function (jqXhr) {
                errorDiv.text(JSON.parse(jqXhr.responseText).message);
            }
        });
    } else {
        errorDiv.text("Please, enter text to translate");
    }
}

$(document).on('keydown', function (e) {
    if (e.which === 13) {
        e.preventDefault();
        translate();
    }
});

$(document).ready(function () {
    $('.translateButton').click(function () {
        translate();
    });
});