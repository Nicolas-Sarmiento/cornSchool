const renderEvents = () =>{
    xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.status === 200 && xhr.readyState === 4 ){
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            const data = JSON.parse(response.content);
            console.log(data);
        }
    }

    xhr.open("GET", "http://localhost:8080/cornSchool_war_exploded/event-servlet");
    xhr.send(null);
}

renderEvents();