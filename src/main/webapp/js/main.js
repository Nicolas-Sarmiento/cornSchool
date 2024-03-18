document.addEventListener("DOMContentLoaded", () => {
    refreshTable();
    chargeDisciplines();
});

function refreshTable() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/cornSchool_war_exploded/ParticipantServlet");

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const participants = JSON.parse(xhr.responseText);
            const tbody = document.getElementById("tbody");
            tbody.innerHTML = "";

            participants.forEach(participant => {
                const row = document.createElement("tr");


                const participantData = {
                    id: participant.id, name: participant.name, age: participant.age, gender: participant.gender,discipline: participant.disciplineId, mail: participant.mail,
                    height: participant.height, weight: participant.weight
                }

                const attributes = ["id", "name", "age", "gender", "discipline", "mail", "height", "weight", "events", "update/delete"];

                attributes.forEach(attribute => {
                    const cell = document.createElement("td");

                    if (attribute === "gender") {
                        const booleanGender = participant[attribute] ? "Hombre" : "Mujer";
                        cell.textContent = booleanGender;
                    }else if (attribute === "update/delete") {
                        const buttonUpdate = document.createElement("button");
                        const buttonDelete = document.createElement("button");
                        buttonUpdate.setAttribute("type", "button");
                        buttonUpdate.setAttribute("class", "btn btn-primary");
                        buttonUpdate.setAttribute("data-bs-toggle", "modal");
                        buttonUpdate.setAttribute("data-bs-target", "#updateModal");
                        buttonUpdate.textContent = "Update";

                        buttonUpdate.addEventListener("click", () => {
                            openUpdateModal(participantData)
                        })

                        buttonDelete.setAttribute("type", "button");
                        buttonDelete.setAttribute("class", "btn btn-primary");
                        buttonDelete.setAttribute("data-bs-toggle", "modal");
                        buttonDelete.setAttribute("data-bs-target", "#deleteModal");
                        buttonDelete.textContent = "Delete";

                        buttonDelete.addEventListener("click", () => {
                            openDeleteModal(participant.id);
                        });

                        cell.appendChild(buttonUpdate);
                        cell.appendChild(buttonDelete);

                    } else {
                        cell.textContent = participant[attribute];
                    }

                    row.appendChild(cell);
                });

                tbody.appendChild(row);
            });
        }
    };

    xhr.send(null);
}

function chargeDisciplines(){
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

document.querySelector("#addButton").addEventListener("click", () => {
    const id = document.querySelector("#idAdd").value;
    const name = document.querySelector("#nameAdd").value;
    const age = document.querySelector("#ageAdd").value;
    const gender = document.querySelector("#genderSelectadd").value;
    const mail = document.querySelector("#mailAdd").value;
    const weight = document.querySelector("#weightAdd").value;
    const height = document.querySelector("#heightAdd").value;
    const discipline = document.querySelector("#disciplineSelectadd").value;

    const data = {
        id: id,
        name: name,
        age: age,
        gender: gender,
        mail: mail,
        weight: weight,
        height: height,
        discipline: discipline
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/AddParticipant", true);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.success) {
                    alert("Participant added successfully.");
                    refreshTable();

                    document.querySelector("#idAdd").value = "";
                    document.querySelector("#nameAdd").value = "";
                    document.querySelector("#ageAdd").value = "";
                    document.querySelector("#genderSelectadd").value = "";
                    document.querySelector("#mailAdd").value = "";
                    document.querySelector("#weightAdd").value = "";
                    document.querySelector("#heightAdd").value = "";
                    document.querySelector("#disciplineSelectadd").value = "";

                    const modal = document.getElementById('addModal');
                    const bootstrapModal = bootstrap.Modal.getInstance(modal);
                    bootstrapModal.hide();

                } else {
                    alert(response.message);
                }
            } else {
                alert("Error adding participant.");
            }
        }
    };

    xhr.send(JSON.stringify(data));
})

document.querySelector("#deleteParticipant").addEventListener("click", () => {
    const participantId = document.querySelector("#deleteParticipantId").value;
    const data = {
        id: participantId
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/DeleteParticipant", true);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.success) {
                    alert("Participant deleted successfully.");
                    refreshTable();

                    const modal = document.getElementById('deleteModal');
                    const bootstrapModal = bootstrap.Modal.getInstance(modal);
                    bootstrapModal.hide();
                } else {
                    alert("Failed to delete participant.");
                }
            } else {
                alert("Error: " + xhr.status);
            }
        }
    };

    xhr.send(JSON.stringify(data));
});

function openDeleteModal(participantId) {
    document.querySelector("#deleteParticipantId").value = participantId;
    const modal = document.getElementById('deleteModal');
    const bootstrapModal = bootstrap.Modal.getInstance(modal);
    bootstrapModal.show();
}

document.querySelector("#updateParticipant").addEventListener("click", () => {
    const id = document.querySelector("#idParticipantUpdate").value;
    const name = document.querySelector("#nameParticipantUpdate").value;
    const age = document.querySelector("#ageParticipantUpdate").value;

    const genderSelect = document.querySelector("#genderParticipantSelect");
    const gender = genderSelect.options[genderSelect.selectedIndex].value;

    const mail = document.querySelector("#mailParticipantUpdate").value;
    const weight = document.querySelector("#weightParticipantUpdate").value;
    const height = document.querySelector("#heightParticipantUpdate").value;

    const disciplineSelect = document.querySelector("#disciplineParticipantSelect");
    const discipline = disciplineSelect.options[disciplineSelect.selectedIndex].value;


    const updatedParticipant = {
        id: id,
        name: name,
        age: age,
        gender: gender,
        mail: mail,
        weight: weight,
        height: height,
        discipline: discipline
    };

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/cornSchool_war_exploded/EditParticipant", true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response.success) {
                    alert("Participant updated successfully.");
                    refreshTable();

                    const modal = document.getElementById('updateModal');
                    const bootstrapModal = bootstrap.Modal.getInstance(modal);
                    bootstrapModal.hide();

                } else {
                    alert("Failed to update participant.");
                }
            } else {
                alert("Error: " + xhr.status);
            }
        }
    };

    xhr.send(JSON.stringify(updatedParticipant));
});

function openUpdateModal(participantData) {
    const { id, name, age,gender, mail, weight, height, discipline } = participantData;

    document.querySelector("#idParticipantUpdate").value = id;
    document.querySelector("#nameParticipantUpdate").value = name;
    document.querySelector("#ageParticipantUpdate").value = age;
    document.querySelector("#mailParticipantUpdate").value = mail;
    document.querySelector("#weightParticipantUpdate").value = weight;
    document.querySelector("#heightParticipantUpdate").value = height;

    const modal = document.getElementById('updateModal');
    const bootstrapModal = bootstrap.Modal.getInstance(modal);

    const genderSelect = document.querySelector("#genderParticipantSelect");
    genderSelect.value = gender ? "true" : "false";

    const disciplineSelect = document.querySelector("#disciplineParticipantSelect");

    chargeDisciplines()

    disciplineSelect.value = discipline

    bootstrapModal.show();
}