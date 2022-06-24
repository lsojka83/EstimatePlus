const deleteButton = document.getElementById("deleteButton");
const deleteLink = document.getElementById("deleteLink");
document.addEventListener('click', function (e) {

    e.target.removeEventListener("click",function (e) {
        if (e.target.id === deleteLink.id) {
            e.stopPropagation();
            e.preventDefault();
        }
    });

});


function clickCounter(event) {
    console.log('Click number', clickCount);
    clickCount += 1;
    if (clickCount >= 10) {
        event.target.removeEventListener('click', clickCounter);
    }
}


function confirmActionDeleteButton() {
    const confirmAction = confirm("Do You confirm?");
    if (confirmAction) {
        document.addEventListener('click', function (e) {
            if (e.target.id === deleteButton.id) {
                e.target.removeEventListener("click", null);
            }
        });
    } else {
        deleteButton.disabled = "true";
        // document.addEventListener('click', function (e) {
        //     if (e.target.id === deleteButton.id) {
        //         e.preventDefault();
        //     }
        // });
    }
}

function confirmActionDeleteLink() {
    const confirmAction = confirm("Do You confirm?");
    if (confirmAction) {
        document.addEventListener('click', function (e) {
            if (e.target.id === deleteLink.id) {
                e.target.removeEventListener("click", null);
            }
        });    } else {
        // deleteLink.disabled = "true";
        document.addEventListener('click', function (e) {
            if (e.target.id === deleteLink.id) {
                e.stopPropagation();
                e.preventDefault();
            }
        });
    }
}


