
document.addEventListener("DOMContentLoaded", ()=> {
    var toolset = document.getElementsByClassName("toolset")[0];
    var panel = document.getElementById("panel");
    toolset.onclick = (e)=> {
        if (e.target !== toolset && e.target.tagName === "LABEL") {
            panel.innerHTML = ""; // clear the panel
        }
    }
   genQuickCommand();
   document.getElementById("tab-1").onclick = genQuickCommand;
   document.getElementById("tab-2").onclick = genAvatar;
   document.getElementById("tab-3").onclick = genWeapon;
   document.getElementById("tab-4").onclick = genReli;
   document.getElementById("tab-5").onclick = genItem;
   document.getElementById("tab-6").onclick = genMonster;
   document.getElementById("tab-7").onclick = genScene;
   document.getElementById("tab-8").onclick = genQuest;
});