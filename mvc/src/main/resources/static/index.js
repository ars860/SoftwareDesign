function doPost(url, obj) {
    fetch(url, {
        method: "POST",
        body: JSON.stringify(obj)
    })
}

export default doPost;