function translate() {
    let initialText = {};
    initialText['initialLanguage'] = 'EN';
    initialText['targetLanguage'] = 'RU';
    initialText['text'] = $('.initialText').val();
    $.ajax({
        url: 'translation',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(initialText),
        contentType: 'application/json;charset=UTF-8',
        success: function (data, textStatus, request) {
            $('.translatedText').val(data.text);
        }
    });
}

$(document).ready(function () {
    $('.translateButton').click(function () {
        translate();
    });
});