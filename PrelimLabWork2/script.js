// ==========================
// 👥 REGISTERED USERS (CAPS)
// ==========================
const users = [
    { username: "YORU", password: "1234" },
    { username: "NEON", password: "1234" },
    { username: "CHAMBER", password: "1234" },
    { username: "ASTRA", password: "1234" },
    { username: "JETT", password: "1234" }
];

// ==========================
// 🔗 ELEMENTS
// ==========================
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const enterBtn = document.getElementById("enterBtn");
const folderBtn = document.getElementById("folderBtn");
const message = document.getElementById("message");
const timestampText = document.getElementById("timestamp");
const loader = document.getElementById("loader");

// ==========================
// 🔊 SOUNDS
// ==========================
const successSound = new Audio("./success.mp3");
const errorSound = new Audio("./beep.mp3");

// ==========================
// 🧠 STATE
// ==========================
let attendanceFolderHandle = null;
let isLoading = false;
let cooldown = false;

// ==========================
// 🧱 DIVIDER LINE
// ==========================
const divider = "-------------------------------------------------------------------------";

// ==========================
// 🕒 DATE + TIME FORMAT
// ==========================
function getFormattedDateTime() {
    const now = new Date();

    const mm = String(now.getMonth() + 1).padStart(2, "0");
    const dd = String(now.getDate()).padStart(2, "0");
    const yyyy = now.getFullYear();

    const hh = String(now.getHours()).padStart(2, "0");
    const min = String(now.getMinutes()).padStart(2, "0");
    const ss = String(now.getSeconds()).padStart(2, "0");

    return `${mm}/${dd}/${yyyy} ${hh}:${min}:${ss}`;
}

// ==========================
// 🔠 FORCE USERNAME CAPS
// ==========================
usernameInput.addEventListener("input", () => {
    usernameInput.value = usernameInput.value.toUpperCase().trim();
});

// ==========================
// 📁 FOLDER PICKER
// ==========================
folderBtn.addEventListener("click", async () => {
    try {
        attendanceFolderHandle = await window.showDirectoryPicker();
        message.innerText = "Attendance folder selected";
        message.style.color = "#00ff9c";
    } catch {
        message.innerText = "Folder selection cancelled";
        message.style.color = "#ffaa00";
    }
});

// ==========================
// ⌨️ KEYBOARD CONTROLS
// ==========================
document.addEventListener("keydown", (e) => {

    // "/" focuses username
    if (
        e.key === "/" &&
        document.activeElement !== usernameInput &&
        document.activeElement !== passwordInput
    ) {
        e.preventDefault();
        usernameInput.focus();
        return;
    }

    // ENTER behavior
    if (e.key === "Enter" && !isLoading && !cooldown) {

        if (document.activeElement === usernameInput) {
            passwordInput.focus();
            return;
        }

        login();
    }
});

// ==========================
// 🖱️ BUTTON CLICK
// ==========================
enterBtn.addEventListener("click", () => {
    if (!isLoading && !cooldown) login();
});

// ==========================
// 🔐 LOGIN FUNCTION
// ==========================
async function login() {

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    // ❗ Folder required
    if (!attendanceFolderHandle) {
        message.innerText = "Please select attendance folder first";
        message.style.color = "#ffaa00";
        return;
    }

    // ❗ Empty input
    if (username === "" || password === "") {
        message.innerText = "Please enter username and password";
        message.style.color = "#ffaa00";
        return;
    }

    // 🔒 Lock UI
    isLoading = true;
    cooldown = true;

    loader.classList.remove("hidden");
    enterBtn.disabled = true;
    usernameInput.disabled = true;
    passwordInput.disabled = true;

    message.innerText = "Authenticating...";
    message.style.color = "#aaa";

    // ⏳ Loading delay
    setTimeout(async () => {

        loader.classList.add("hidden");
        enterBtn.disabled = false;
        usernameInput.disabled = false;
        passwordInput.disabled = false;
        isLoading = false;

        const matchedUser = users.find(
            u => u.username === username && u.password === password
        );

        if (!matchedUser) {
            // ❌ WRONG LOGIN
            errorSound.play();
            message.innerText = "ACCESS DENIED";
            message.style.color = "#ff4655";

            document.body.classList.add("error-bg");
            setTimeout(() => {
                document.body.classList.remove("error-bg");
            }, 400);

        } else {
            // ✅ SUCCESS
            successSound.play();

            const now = getFormattedDateTime();
            timestampText.innerText = "Timestamp: " + now;

            const fileHandle = await attendanceFolderHandle.getFileHandle(
                "login_records.txt",
                { create: true }
            );

            const file = await fileHandle.getFile();
            let text = await file.text();

            let lines = text.split("\n").filter(l => l.trim() !== "");
            let updated = false;
            let isLogout = false;

            // 🔁 TIME OUT if open
            for (let i = lines.length - 1; i >= 0; i--) {
                if (lines[i].startsWith(username) && lines[i].includes("TIME OUT : ---")) {
                    lines[i] = lines[i].replace(
                        "TIME OUT : ---",
                        "TIME OUT : " + now
                    );
                    updated = true;
                    isLogout = true;
                    break;
                }
            }

            // 🆕 NEW TIME IN
            if (!updated) {
                lines.push(
                    `${username.padEnd(10)} | TIME IN : ${now} | TIME OUT : ---`
                );
                lines.push(divider);
                lines.push("");
            }

            // 📝 WRITE FILE
            const writable = await fileHandle.createWritable();
            await writable.write(lines.join("\n"));
            await writable.close();

            // 🖥️ MESSAGE DISPLAY
            if (isLogout) {
                message.innerHTML =
                    "LOGOUT RECORDED<br><span style='font-size:12px; opacity:0.8;'>SESSION ENDED</span>";
            } else {
                message.innerHTML =
                    "WELCOME STUDENT<br><span style='font-size:12px; opacity:0.8;'>Enter your user and password again to log out.</span>";
            }

            message.style.color = "#00ff9c";

            usernameInput.value = "";
            passwordInput.value = "";
            usernameInput.focus();
        }

        // ⏱️ Cooldown release
        setTimeout(() => {
            cooldown = false;
        }, 1500);

    }, 1500);
}
