'use strict';

app.controller('Carousel', function ($scope) {
    console.log("Carousel");
    $scope.slides = [
        {
            "url": "images/carousel/slide1.jpg",
            "header": "Track all your expenses and bills",
            "text": null
        },
        {
            "url": "images/carousel/slide2.jpg",
            "header": "Get statistics by multiple chart types",
            "text": null
        },
        {
            "url": "images/carousel/slide4.jpg",
            "header": "Stop to waste money and start to save",
            "text": null
        }]
});