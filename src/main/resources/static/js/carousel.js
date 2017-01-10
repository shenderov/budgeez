'use strict';

app.controller('Carousel', function ($scope) {
    $scope.slides = [
        {
            "url": "./../../images/slide1.jpg",
            "header": "Track all your expenses and bills",
            "text": null
        },
        {
            "url": "./../../images/slide2.jpg",
            "header": "Get statistics by multiple chart types",
            "text": null
        },
        {
            "url": "./../../images/slide4.jpg",
            "header": "Stop waste money and start to save",
            "text": "test"
        }]
});