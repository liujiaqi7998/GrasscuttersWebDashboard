var delayedSearch = null;

function genQuest() {
    var panel = document.getElementById("panel");
    panel.innerHTML = `
    <div class="mui-card-header">添加任务(测试)</div>
    <div class="mui-card-content">
        <ul class="mui-table-view">
            <li class="mui-table-view-cell">
                <div style="display: flex; flex-direction: column;">
                    <label for="entity-search">任务名称:</label>
                    <div style="display: flex; align-items: center; overflow: hidden;">
                        <input id="entity-search" style="flex: 4" type="text" placeholder="搜索任务名称，绿色为测试可以" />
                        <button id="clear" class="mui-btn mui-btn-danger mui-btn-outlined" style="margin-left: 0.25em; transition: all ease-in-out 0.5s; flex: 0; opacity: 0;">清除</button>
                    </div>
                    <div id="name-list" style="overflow-y: auto; overflow-x: hidden; max-height: 10em;height: 100%; transition: all ease-in-out 0.5s;">
                    </div>
                </div>
            </li>
            <li class="mui-table-view-cell"><input style="display: none" id="entity-id" /></li>
        </ul>
    </div>
    <div class="mui-card-footer">
            <button disabled id="execute">添加任务</button>
            <button disabled id="execute-safe">完成任务</button>
     </div>`;

    updateQuestList();

    document.getElementById("entity-search").oninput = (e) => {
        if (e.target.value.length > 0) {
            document.getElementById("clear").style.flex = 1;
            document.getElementById("clear").style.opacity = 1;
        } else {
            document.getElementById("clear").style.flex = 0;
            document.getElementById("clear").style.opacity = 0;
        }
        if (delayedSearch) {
            clearTimeout(delayedSearch);
        }
        delayedSearch = setTimeout(() => updateQuestList(), 500);
        document.getElementById("name-list").style.height = "10em";
    };
    document.getElementById("clear").onclick = () => {
        document.getElementById("entity-search").value = "";
        updateQuestList();
    }
    document.getElementById("entity-search").onkeydown = (e) => {
        if (e.key === "Escape") {
            document.getElementById("entity-search").value = "";
            updateQuestList();
        }
    }
    document.getElementById("entity-id").setvalue = (v) => {
        document.getElementById("entity-id").value = v;
        if (v) {
            document.getElementById("execute").disabled = false;
            document.getElementById("execute-safe").disabled = false;
        } else {
            document.getElementById("execute").disabled = true;
            document.getElementById("execute-safe").disabled = true;
        }
    }
    document.getElementById("name-list").onclick = (e) => {
        if (e.target.tagName === "INPUT") {
            const list = document.getElementById("name-list");
            list.style.height = "3em";
            const name = e.target.attributes['entity-name'].nodeValue ? e.target.attributes['entity-name'].nodeValue : "UNKNOWN";
            const color = {0: 'blue', 1: 'orange'}[e.target.attributes['entity-level'].nodeValue];
            list.innerHTML = `<input name="stack" type="radio" name="entity-id" entity-id="${e.target.attributes['entity-id'].nodeValue}" entity-name="${e.target.attributes['entity-name'].nodeValue}">
                <span class="button quality-${color}">
                ${name} - ${e.target.attributes['entity-id'].nodeValue}
                </span>`;
            document.getElementById("entity-search").value = name;
            document.getElementById("clear").style.flex = 1;
            document.getElementById("clear").style.opacity = 1;
            document.getElementById("entity-id").setvalue(e.target.attributes['entity-id'].nodeValue);

        }
    }
    // updateWeaponList();
    // document.getElementById("weapon-filter").onchange = updateWeaponList;
    document.getElementById("execute").onclick = () => {
        const entityId = document.getElementById("entity-id").value;
        sendCommand(`quest add ${entityId}`);
    }
    document.getElementById("execute-safe").onclick = () => {
        const entityId = document.getElementById("entity-id").value;
        sendCommand(`quest finish ${entityId}`);
    }
}

function updateQuestList() {
    const filter = document.getElementById("entity-search").value;
    const list = document.getElementById("name-list");
    list.innerHTML = "";
    list.style.height = "10em";
    Quest_data.forEach(element => {
        if (filter === "" || element.name.toLowerCase().indexOf(filter.toLowerCase()) !== -1) {
            const o = document.createElement("label");
            o.style.marginLeft = "0.1em";
            var color = {0: 'primary', 1: 'orange', 2: 'success', 3: 'primary', 4: 'royal', 5: 'warning'}[element.type];
            o.innerHTML = `<input name="stack" type="radio" name="entity-id" entity-id="${element.id}" entity-name="${element.name}" entity-level="${element.type}">
                <span class="mui-btn mui-btn-outlined mui-btn-${color}">
                ${element.name ? element.name : "UNKNOWN"}
                </span>`;
            list.appendChild(o);
        }

    });

}
