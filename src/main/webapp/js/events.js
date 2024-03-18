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

    openEditDisciplineModal

    buttonEdit.addEventListener("click", () => {
        const array = JSON.parse(participants);
        const participantIds = array.map(participant => participant.id).join(",");
        openEditDisciplineModal(id, name, description, date, discipline.id, participantIds);
    });

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

                const select2 = document.getElementById('disciplineSelectedUpdate');

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

document.getElementById("addDisciplineButton").addEventListener("click", () => {
    const id = document.getElementById("idDisciplineAdd").value;
    const name = document.getElementById("nameDisciplineAdd").value;
    const description = document.getElementById("descriptionDisciplineAdd").value;
    const date = document.getElementById("dateDisciplineAdd").value;
    const discipline = document.getElementById("disciplineParticipantSelect").value;
    const participantsIds = document.getElementById("participantsIdAdd").value.split(",").map(id => id.trim());

    const data = {
        id: id,
        name: name,
        description: description,
        date: date,
        idDiscipline: discipline,
        leaderboard: participantsIds
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/AddEvent-servlet");
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                alert(response.message);

                document.getElementById("idDisciplineAdd").value = "";
                document.getElementById("nameDisciplineAdd").value = "";
                document.getElementById("descriptionDisciplineAdd").value = "";
                document.getElementById("dateDisciplineAdd").selectedIndex = 0;
                document.getElementById("disciplineParticipantSelect").value = "";
                document.getElementById("participantsIdAdd").value = "";
            } else {
                alert("La solicitud ha fallado");
            }
            readEvents()
        }
    };
    xhr.send(JSON.stringify(data));
});

document.getElementById("editDisciplinesButton").addEventListener("click", () => {
    const id = document.getElementById("idDisciplineUpdate").value;
    const name = document.getElementById("nameDisciplineUpdate").value;
    const description = document.getElementById("descriptionDisciplineUpdate").value;
    const date = document.getElementById("dateDisciplineUpdate").value;
    const discipline = document.getElementById("disciplineSelectedUpdate").value;
    const participantsIds = document.getElementById("participantsIdUpdate").value.split(",").map(id => id.trim());

    const data = {
        id: id,
        name: name,
        description: description,
        date: date,
        idDiscipline: discipline,
        leaderboard: participantsIds
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/EditEvent-servlet");
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                alert(response.message);

                document.getElementById("idDisciplineAdd").value = "";
                document.getElementById("nameDisciplineAdd").value = "";
                document.getElementById("descriptionDisciplineAdd").value = "";
                document.getElementById("dateDisciplineAdd").selectedIndex = 0;
                document.getElementById("disciplineParticipantSelect").value = "";
                document.getElementById("participantsIdAdd").value = "";
            } else {
                alert("La solicitud ha fallado");
            }
            readEvents()
        }
    };
    xhr.send(JSON.stringify(data));
});

const openEditDisciplineModal = (id, name, description, date, disciplineId, participantsId) => {
    const modal = document.getElementById('updateDisciplinesModal');

    const formattedDate = formatDate(date);


    modal.querySelector("#idDisciplineUpdate").value = id;
    modal.querySelector("#nameDisciplineUpdate").value = name;
    modal.querySelector("#descriptionDisciplineUpdate").value = description;
    modal.querySelector("#dateDisciplineUpdate").value = formattedDate;

    loadDisciplines()

    modal.querySelector("#disciplineSelectedUpdate").value = disciplineId;
    modal.querySelector("#participantsIdUpdate").value = participantsId;

    const bootstrapModal = new bootstrap.Modal(modal);
    bootstrapModal.show();
};

const formatDate = (dateString) => {
    const parts = dateString.split('-');
    return `${parts[2]}-${parts[1]}-${parts[0]}`;
};

const openDeleteDisciplineModal = (id) => {
    document.querySelector("#deleteDisciplineId").value = id;
    const modal = document.getElementById('deleteDescriptionModal');
    let bootstrapModal = bootstrap.Modal.getInstance(modal);
    if (!bootstrapModal) {
        bootstrapModal = new bootstrap.Modal(modal);
    }
    bootstrapModal.show();
}

document.querySelector("#deleteDisciplineButton").addEventListener("click", () => {
    const disciplineId = document.querySelector("#deleteDisciplineId").value;
    const data = {
        id: disciplineId
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/DeleteEvent-servlet", true);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.message === "Discipline deleted successfully") {
                    alert("Discipline deleted successfully.");
                    bootstrapModal.hide();
                } else {
                    alert(response.message);
                }
                readEvents()
            } else {
                alert("Error: " + xhr.status);
            }
        }
    };

    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(data));
});



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
