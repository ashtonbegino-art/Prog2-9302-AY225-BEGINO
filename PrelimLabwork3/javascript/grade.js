function calculateGrade() {
    // ===== CONSTANTS =====
    const TOTAL_MEETINGS = 10;

    // ===== GET INPUTS =====
    const attendance = parseInt(document.getElementById('attendance').value);
    const lab1 = parseFloat(document.getElementById('lab1').value);
    const lab2 = parseFloat(document.getElementById('lab2').value);
    const lab3 = parseFloat(document.getElementById('lab3').value);

    // ===== VALIDATION =====
    if (
        isNaN(attendance) || isNaN(lab1) ||
        isNaN(lab2) || isNaN(lab3)
    ) {
        alert('Please fill in all fields with valid numbers.');
        return;
    }

    if (attendance < 0 || attendance > TOTAL_MEETINGS) {
        alert('Attendance must be between 0 and 10.');
        return;
    }

    if (
        lab1 < 0 || lab1 > 100 ||
        lab2 < 0 || lab2 > 100 ||
        lab3 < 0 || lab3 > 100
    ) {
        alert('Lab grades must be between 0 and 100.');
        return;
    }

    // ===== ATTENDANCE RULE =====
    const absences = TOTAL_MEETINGS - attendance;

    // ===== DISPLAY INPUT SUMMARY (ALWAYS SHOW) =====
    document.getElementById('displayAttendance').textContent = attendance.toFixed(2);
    document.getElementById('displayLab1').textContent = lab1.toFixed(2);
    document.getElementById('displayLab2').textContent = lab2.toFixed(2);
    document.getElementById('displayLab3').textContent = lab3.toFixed(2);

    // ===== FAIL DUE TO ATTENDANCE =====
  if (absences >= 3 || attendance <= 7) {
    document.getElementById('labAverage').textContent = "FAILED";
    document.getElementById('classStanding').textContent = "FAILED";
    document.getElementById('requiredPass').textContent = "N/A";
    document.getElementById('requiredExcellent').textContent = "N/A";

    const remarksElement = document.getElementById('remarks');
    remarksElement.className = 'remarks danger';
    document.getElementById('remarksContent').innerHTML = `
        <p style="font-size:1.1em">❌ <strong>STUDENT FAILED</strong></p>
        <p>Reason: Attendance rule violated.</p>
        <p><strong>Attendance:</strong> ${attendance} / 10</p>
        <p><strong>Absences:</strong> ${absences}</p>
        <p>Minimum required attendance is 8.</p>
    `;

    document.getElementById('resultSection').classList.add('show');
    return;
}


    // ===== CALCULATIONS =====
    const labAverage = (lab1 + lab2 + lab3) / 3;
    const classStanding = (0.40 * attendance) + (0.60 * labAverage);

    const requiredForPassing = (75 - (0.70 * classStanding)) / 0.30;
    const requiredForExcellent = (100 - (0.70 * classStanding)) / 0.30;

    // ===== DISPLAY COMPUTED VALUES =====
    document.getElementById('labAverage').textContent = labAverage.toFixed(2);
    document.getElementById('classStanding').textContent = classStanding.toFixed(2);
    document.getElementById('requiredPass').textContent = requiredForPassing.toFixed(2);
    document.getElementById('requiredExcellent').textContent = requiredForExcellent.toFixed(2);

    // ===== REMARKS =====
    let remarksHTML = '';
    let remarksClass = 'info';

    if (requiredForPassing > 100) {
        remarksHTML += `
            <p>❌ <strong>Status: FAILED</strong></p>
            <p>Passing is mathematically impossible.</p>
        `;
        remarksClass = 'danger';
    } else {
        remarksHTML += `
            <p>📝 You need <strong>${requiredForPassing.toFixed(2)}</strong>
            to pass the Prelim Exam.</p>
        `;
    }

    if (requiredForExcellent > 100) {
        remarksHTML += `
            <p>⚠️ Achieving an Excellent grade (100) is not possible.</p>
        `;
    } else {
        remarksHTML += `
            <p>🌟 You need <strong>${requiredForExcellent.toFixed(2)}</strong>
            for an Excellent grade.</p>
        `;
    }

    const remarksElement = document.getElementById('remarks');
    remarksElement.className = 'remarks ' + remarksClass;
    document.getElementById('remarksContent').innerHTML = remarksHTML;

    document.getElementById('resultSection').classList.add('show');
}
