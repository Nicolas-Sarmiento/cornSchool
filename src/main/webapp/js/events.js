document.addEventListener("DOMContentLoaded", () => {
    readEvents();
    loadDisciplines();
});
const readEvents = () =>{
    xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/cornSchool_war_exploded/event-servlet");

    xhr.onreadystatechange = () => {
        if (xhr.status === 200 && xhr.readyState === 4 ){
            const response = JSON.parse(xhr.responseText);
            if (!response.status) return;
            const data = JSON.parse(response.content);
            console.log(data)
            const container = document.getElementById("main");
            container.innerHTML = "";
            data.forEach( item => {
                container.appendChild(renderEvent(item.id, item.name, item.description, item.date, item.discipline, item.leaderboard));
            });

        }
    }

    xhr.send(null);
}

const renderEvent = ( id, name, description, date, discipline, participants) => {
    const container = document.createElement("div");
    container.setAttribute("class", "container-fluid mb-5");

    const titleContent = document.createElement("div");
    titleContent.setAttribute("class", "d-flex mb-3");
    const title = document.createElement("h3");
    title.setAttribute("class", "me-auto p-2");
    title.appendChild(document.createTextNode(name));

    const buttonEdit = document.createElement("button");
    const buttonDelete = document.createElement("button");
    buttonEdit.setAttribute("class","btn btn-primary");
    buttonDelete.setAttribute("class","btn btn-primary");

    buttonEdit.appendChild(document.createTextNode("Edit"));
    buttonDelete.appendChild(document.createTextNode("Delete"));

    buttonEdit.addEventListener("click", () => {
        openEditDisciplineModal(id, name, description, date);
    });

    buttonDelete.addEventListener("click", () => {
        openDeleteDisciplineModal(id)
    })

    const dateParragraph = document.createElement("p");
    dateParragraph.appendChild(document.createTextNode(`date: ${date}`));

    const disciplineParragraph = document.createElement("p");
    disciplineParragraph.appendChild(document.createTextNode(`discipline: ${discipline}`));

    const descriptionP = document.createElement("p");
    descriptionP.appendChild(document.createTextNode(`description: ${description}`));


    const array = JSON.parse(participants);
    let table;
    if (array.length === 0){
        table = document.createElement("br");
    }else {
        table = renderParticipants( array );
    }

    const divider = document.createElement("hr");
    divider.setAttribute("class", "hr-blurry");

    titleContent.appendChild(title);
    titleContent.appendChild(buttonEdit);
    titleContent.appendChild(buttonDelete);
    container.appendChild(titleContent);
    container.appendChild(dateParragraph);
    container.appendChild(disciplineParragraph);
    container.appendChild(descriptionP);
    container.appendChild(table);
    container.appendChild(divider);
    return container;
}

const renderParticipants = ( participants ) => {
    const table = document.createElement("table");
    table.setAttribute("class", "table table-striped-columns");

    const headerRow = document.createElement("thead");
    const titles = ["position","id", "name"];
    titles.forEach( attribute => {
        const td = document.createElement("td");
        td.appendChild(document.createTextNode(attribute));
        td.setAttribute("scope","col");
        headerRow.appendChild(td);
    });

    const tbody = document.createElement("tbody");
    participants.sort(compareLeaderboard);
    participants.forEach( data => {
        const row = document.createElement("tr");
        titles.forEach( attribute => {
            const td = document.createElement("td");

            td.appendChild(document.createTextNode(data[attribute]));

            row.appendChild(td);
        });
        tbody.appendChild(row);
    });
    table.appendChild(headerRow);
    table.appendChild(tbody);

    return table;
}

const compareLeaderboard = (a, b) => {
    if ( a.position < b.position ){
        return -1;
    }
    if ( a.position > b.position ){
        return 1;
    }
    return 0;
}

const loadDisciplines = () => {
    const xhr = new XMLHttpRequest();

    xhr.open("GET", "http://localhost:8080/cornSchool_war_exploded/DisciplinesServlet");

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if( xhr.responseText !== `null` ){
                const disciplines  =JSON.parse(xhr.responseText)

                const select = document.getElementById('disciplineParticipantSelect');

                select.innerHTML = '';

                disciplines.forEach(discipline => {
                    const option = document.createElement('option');
                    option.value = discipline.id;
                    option.textContent = discipline.name;
                    select.appendChild(option);
                });

                const select2 = document.getElementById('disciplineSelectadd');

                select2.innerHTML = '';

                disciplines.forEach(discipline => {
                    const option = document.createElement('option');
                    option.value = discipline.id;
                    option.textContent = discipline.name;
                    select2.appendChild(option);
                });

            }
        }
    };
    xhr.send(null)
}


// add and edit Request
// data = {
//     "id":"2",
//     "name": "event name",
//     "description": "event description",
//     "date": "dd-MM-yyyy",
//     "discipline": "discipline id",
//     "leaderboard": ["id first participant", "id second participant ... "]
// }

// delete Request
// data = {
//     "id":"id event to delete"
// }
