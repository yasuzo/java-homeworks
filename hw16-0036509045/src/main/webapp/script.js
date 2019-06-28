window.onload = loadTags;

/**
 * Loads image tags and places them as buttons.
 */
function loadTags() {
    console.log("Loading tags");
    makeJsonRequest('GET', 'rest/image/tags', (tags) => {
        let tagBox = document.getElementById("tag-box");
        tags.forEach((item) => {
            let button = document.createElement('button');
            button.addEventListener('click', () => loadThumbnails(item));
            button.innerHTML = item;
            tagBox.appendChild(button);
        });
    });
}


/**
 * Loads thumbnails of images for given tag name.
 *
 * @param tagName Selected tag name.
 */
function loadThumbnails(tagName) {
    makeJsonRequest('GET', 'rest/image/tags/' + tagName, (thumbnails) => {
        document.getElementById("image-box").innerHTML = "";
        let thumbnailBox = document.getElementById("thumbnail-box");
        thumbnailBox.innerHTML = "";
        thumbnails.forEach((item) => {
            let img = document.createElement('img');
            img.setAttribute('src', "thumbnail?image=" + item.name);
            img.addEventListener('click', () => loadFullSizeImage(item.name));
            thumbnailBox.appendChild(img);
        });
    });
}

/**
 * Loads full size image and renders it.
 *
 * @param imageName Image name.
 */
function loadFullSizeImage(imageName) {
    makeJsonRequest('GET', 'rest/image/' + imageName, (image) => {
        let imageBox = document.getElementById("image-box");
        imageBox.innerHTML = "<h3>" + image.name + "</h3>";
        imageBox.innerHTML += "<img src='image?image=" + image.name + "'/>";
        imageBox.innerHTML += "<p>" + image.description + "</p>"
        imageBox.innerHTML += "<p> Tags: " + image.tags.join(", ") + "</p>";
    });
}

/**
 * Makes an ajax request expecting JSON response.
 *
 * @param method Request method.
 * @param url Where should request be sent.
 * @param processFunction Function which will be called on success and will get parsed JSON.
 */
function makeJsonRequest(method, url, processFunction) {
    let httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (httpRequest.status !== 200) {
            return;
        }
        processFunction(JSON.parse(httpRequest.responseText));
    };

    httpRequest.open(method, url);
    httpRequest.send();
}