var filterMethod = "set";

function genReli() {
    var panel = document.getElementById("panel");
    panel.innerHTML = `
    <div class="mui-card-header">给自己发送圣遗物</div>
    <div class="mui-card-content">
        <ul class="mui-table-view">
            <li class="mui-table-view-cell">
                <label>圣遗物名称：</label>
            </li>
            <li class="mui-table-view-cell">
                <div> 检索方式：
                    <input id="by-set" type="radio" name="filter_type" checked /> <label for="by-set" class="checkable">按套装</label> 
                    <input id="by-name" type="radio" name="filter_type" />  <label for="by-name" class="checkable">按名称</label> 
                    <button id="clear-reli-search" class="hidden by-name">清除</button>
                </div>
                <div style="display: flex;" id="search-box" class="search-box">
                    <div style="flex: 5;">
                        <select id="reli-set" class="by-set">
                            <option value="0">套装：全部</option>
                        </select>
                        <input id="reli-search" style="margin-top: 0.25em" type="text" class="hidden by-name" placeholder="搜索圣遗物名称" />
                    </div>
                    <div style="flex: 5; margin-left: 0.5em" class="by-set">
                        <select id="reli-quality" class="by-set">
                        </select>
                    </div>
                    <div style="flex: 5; margin-left: 0.5em">
                        <select id="reli-select"  class="by-set">
                        </select>
                        <div class="by-name" id="name-list" style="overflow-y: auto; overflow-x: hidden; height: 100%;">
                        </div>
                    </div>
                </div>
            </li>      
            <input id="reli-id" type="text" style="display: none;" />
            <li class="mui-table-view-cell">
                <label for="level">强化等级:</label>
                <input type="number" id="level" name="level" value=20 />
            </li>  
            <li class="mui-table-view-cell">
                <div style="display: flex; flex-direction: column; margin-top: 1em;">
                    <div style="flex: 2; padding: 0.5em;" class="card">
                        <label for="main-prop">主属性：</label>
                        <div style="display: flex; align-items: end">
                            <div style="flex: 4;">
                                <select id="main-prop"> </select>
                            </div>
                            <div style="flex: 2; overflow: hidden; transition: all ease-in-out 0.5s; max-width: 3em; margin-bottom: 0.3em; margin-left: 0.5em" id="percent-modifier">
                                <input type="checkbox" id="main-percent" name="main-percent" />
                                <label for="main-percent" class="checkable">%</label>
                            </div>
                        </div>
                    </div>
                    <div style="flex: 4;padding: 0.5em;" class="card">
                        <label for="append-prop">副属性：</label>
                        <table style="width: 100%">
                            <thead>
                                <tr><th style="width:50%">属性</th><th style="width:30%">数值</th><th style="width:20%">次数</th><th style="width: 5%"></th></tr>
                            </thead>
                            <tbody id="tbody-affix-prop">
                               
                            </tbody>
                            <tr>
                                <td>
                                    <select id="affix-prop">
                                    </select>
                                </td>
                                <td><select id="affix-value"><option>15.5</option> </select></td>
                                <td><input type="number" value="1" id="affix-time" /></td>
                                <td><button id="add-affix">+</a></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </li>  
        </ul>
    </div>
    <div class="mui-card-footer">
            <button id="execute">发送</button>
     </div>`;
    document.getElementById("reli-id").setvalue = updateReliId;
    var filter = document.getElementById("reli-set");
    reli_list.forEach(reli => {
        var o = document.createElement("option");
        o.value = reli.id;
        o.innerText = "套装：" + reli.name;
        filter.appendChild(o);
    })
    document.getElementById("reli-set").oninput = updateQualityList;
    document.getElementById("reli-quality").oninput = () => {
        updateReliList();
        updateReliId(document.getElementById("reli-select").value);
    };
    document.getElementById("reli-select").oninput = (e) => {
        document.getElementById("reli-id").setvalue(e.target.value);
    };
    document.getElementById("reli-search").oninput = updateQualityList;
    document.getElementById("clear-reli-search").onclick = () => {
        document.getElementById("reli-search").value = "";
        updateQualityList();
    };
    document.getElementById("name-list").onclick = (e) => {
        if (e.target.tagName == "INPUT") {
            console.log(e.target);
            document.getElementById("reli-search").value = e.target.attributes['reli-name'].nodeValue;
            document.getElementById("reli-id").setvalue(e.target.attributes['reli-id'].nodeValue, e.target.attributes['reli-quality'].nodeValue);
            setTimeout(() => {
                document.getElementById("search-box").style.height = "3em";
                var color = "quality-" + {
                    0: "white",
                    1: "green",
                    2: "blue",
                    3: "purple",
                    4: "orange",
                    5: "unknown"
                }[e.target.attributes['reli-quality'].nodeValue - 1];
                var content = `<input name="stack" type="radio" name="reli-id" reli-id="${e.target.attributes['reli-id'].nodeValue}" reli-name="${e.target.attributes['reli-name'].nodeValue}" reli-quality="${e.target.attributes['reli-quality'].nodeValue}">
                <span class="button ${color}">
                ${e.target.attributes['reli-name'].nodeValue}
                </span>`;
                document.getElementById("name-list").innerHTML = content;
            }, 10);
            updateQualityList();
        }
    };
    document.getElementById("by-set").onchange = () => {
        filterMethod = "set";
        Array.prototype.forEach.call(document.getElementsByClassName("by-set"), (e) => {
            e.classList.remove("hidden");
        });
        Array.prototype.forEach.call(document.getElementsByClassName("by-name"), (e) => {
            e.classList.add("hidden");
        });
        document.getElementById("search-box").style.height = "3em";
        updateReliList();
    };
    document.getElementById("by-name").onchange = () => {
        filterMethod = "name";
        Array.prototype.forEach.call(document.getElementsByClassName("by-name"), (e) => {
            e.classList.remove("hidden");
        });
        Array.prototype.forEach.call(document.getElementsByClassName("by-set"), (e) => {
            e.classList.add("hidden");
        });
        updateQualityList();
        // initital value
    };
    document.getElementById("main-prop").oninput = updateMainProp;
    document.getElementById("affix-prop").oninput = updateAffixProp;
    document.getElementById("add-affix").onclick = addAffixProp;

    document.getElementById("execute").onclick = () => {
        var reliId = document.getElementById("reli-id").value;
        var level = document.getElementById("level").value;
        var mainPropSelecter = document.getElementById("main-prop");
        var mainPropId = parseInt(mainPropSelecter.value);
        if (document.getElementById("main-percent").checked) {
            mainPropId += parseInt(mainPropSelecter.options[mainPropSelecter.selectedIndex].attributes['percent'].nodeValue);
        }
        var affix = "";
        Array.prototype.forEach.call(document.getElementsByClassName("affix-data"), (e) => {
            affix += " " + e.value;
        })
        sendCommand(`giveart ${reliId} ${mainPropId} ${affix} ${parseInt(level) + 1}`.replace("  ", " "));
    }
    document.getElementById("by-set").onchange();
    updateQualityList();
}

// ----------------------------------------------------------------
function updateQualityList() {
    if (filterMethod == "set") {
        var filter = document.getElementById("reli-set").value;
        var quality = document.getElementById("reli-quality");
        quality.innerHTML = "";
        var qualities = [null, null, null, null, null, null];
        reli_list.forEach(element => {
            if (filter == 0 || element.id == filter) {
                for (var i = 0; i < qualities.length; i++) {
                    if (element.contains[i + 1]) {
                        var o = document.createElement("option");
                        o.value = i + 1;
                        o.innerText = "质量: " + {0: "白色", 1: "绿色", 2: "蓝色", 3: "紫色", 4: "金色", 5: "未知"} [i];
                        qualities[i] = o;
                    }
                }
            }
        });
        qualities.forEach(e => {
            if (e) {
                quality.appendChild(e);
            }
        })
        updateReliList();
        document.getElementById("reli-id").setvalue(document.getElementById('reli-select').value);

    } else {
        var list = document.getElementById("name-list");
        var keyword = document.getElementById("reli-search").value;
        if (keyword.length > 0) {
            document.getElementById("clear-reli-search").classList.remove('hidden');
        } else {
            document.getElementById("clear-reli-search").classList.add('hidden');
        }
        list.innerHTML = "";
        reli_list.forEach(element => {
            Object.entries(element.contains).forEach(quality => {
                quality[1].forEach(item => {
                    if (keyword == "" || item.name.toLowerCase().indexOf(keyword.toLowerCase()) !== -1) {
                        var o = document.createElement("label");
                        var color = "quality-" + {
                            0: "white",
                            1: "green",
                            2: "blue",
                            3: "purple",
                            4: "orange",
                            5: "unknown"
                        }[quality[0] - 1];
                        o.innerHTML = `
                            <input name="stack" type="radio" name="reli-id" reli-id="${item.id}" reli-name="${item.name}" reli-quality="${quality[0]}">
                            <span class="button ${color}">
                            ${item.name}
                            </span>
                        `;
                        list.appendChild(o);
                    }
                })
            })
        })
        document.getElementById("search-box").style.height = "10em";
    }
}

function updateReliList() {
    var filter = document.getElementById("reli-set").value;
    var quality = document.getElementById("reli-quality").value;
    var select = document.getElementById("reli-select");
    select.innerHTML = "";
    reli_list.forEach(reli_set => {
        if (filter == 0 || reli_set.id == filter) {
            if (reli_set.contains[quality]) {
                reli_set.contains[quality].forEach(entry => {
                    var o = document.createElement("option");
                    o.value = entry.id;
                    o.innerText = entry.name;
                    select.appendChild(o);
                });
            }

        }
    })
}

function updateReliId(reliID, quality) {
    document.getElementById("reli-id").value = reliID;
    if (!quality) quality = document.getElementById("reli-quality").value;
    var mainPropSelect = document.getElementById("main-prop");
    var affixPropSelect = document.getElementById("affix-prop");
    var tbody = document.getElementById("tbody-affix-prop");

    mainPropSelect.innerHTML = "";
    affixPropSelect.innerHTML = "";
    tbody.innerHTML = "";
    reli_list.forEach(e => {
        if (!e.contains[quality]) {
            return;
        }
        e.contains[quality].forEach(item => {
            if (item.id == reliID) {
                // console.log(reli_main_prop[item.main]);
                Object.entries(reli_main_prop[item.main]).forEach(pname => {
                    var o = document.createElement("option");
                    o.innerText = pname[0]
                    o.value = pname[1]["normal"];
                    if (pname[1]["percent"]) {
                        o.setAttribute("percent", pname[1]["percent"] - pname[1]["normal"])
                    }
                    mainPropSelect.appendChild(o)
                });
                Object.entries(reli_affix_prop[item.append]).forEach(pname => {
                    var o = document.createElement("option");
                    o.innerText = pname[0];
                    o.setAttribute("values", JSON.stringify(pname[1]['normal']));
                    affixPropSelect.appendChild(o);
                    if (pname[1]['percent']) {
                        o = document.createElement("option");
                        o.innerText = pname[0] + " %";
                        o.setAttribute("values", JSON.stringify(pname[1]["percent"]));
                        o.setAttribute("percent", true);
                        affixPropSelect.appendChild(o);
                    }
                });
            }
        })
    })
    updateMainProp();
    updateAffixProp();
}

function updateMainProp() {
    var mainProp = document.getElementById("main-prop");
    var percent = document.getElementById("percent-modifier");
    var o = mainProp.options[mainProp.selectedIndex];
    if (o.attributes['percent']) {
        percent.style.maxWidth = "3em";
    } else {
        document.getElementById("main-percent").checked = false;
        percent.style.maxWidth = "0em";
    }

}

function updateAffixProp() {
    var affixProp = document.getElementById("affix-prop");
    var affixValue = document.getElementById("affix-value");
    affixValue.innerHTML = "";
    var data = JSON.parse(affixProp.options[affixProp.selectedIndex].attributes["values"].nodeValue);
    var percent = affixProp.options[affixProp.selectedIndex].attributes["percent"] != null;
    Object.entries(data).forEach((e) => {
        var o = document.createElement("option");
        o.value = e[0];
        o.innerText = e[1] < 0.5 ? (e[1] * 100).toFixed(2) + "%" : e[1].toFixed(2);
        affixValue.appendChild(o);
    })

}

function addAffixProp() {
    var tbody = document.getElementById("tbody-affix-prop");
    var affixProp = document.getElementById("affix-prop");
    var affixValue = document.getElementById("affix-value");
    var affixTime = parseInt(document.getElementById("affix-time").value);
    var innerHTML = `
    <tr>
        <td>
            ${affixProp.options[affixProp.selectedIndex].innerText}
        </td>
        <td>
            ${affixValue.options[affixValue.selectedIndex].innerText}
        </td>
        <td>
            ${affixTime}
        </td>
        <td><button class="error">X</button> 
            <input style="display: none;" class="hidden affix-data" value="${affixValue.value},${affixTime}"/>
        </td>
    </tr> `;
    tbody.innerHTML += innerHTML;

    Array.prototype.forEach.call(document.getElementsByClassName("error"), (e) => {
        e.onclick = (event) => {
            var trNode = event.target.parentNode.parentNode;
            trNode.remove();
        }
    });
}