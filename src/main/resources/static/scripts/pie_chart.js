google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {

    const dataTitle = ["Категорія", "Сума"];
    //const reportByCategories = /*[[${reportByCategories}]]*/ null;

    const dataReport = [dataTitle];
    for (let i = 0; i < reportByCategories.length; i++) {
        dataReport.push([reportByCategories[i].category.name, reportByCategories[i].totalSum])
    }

    const data = google.visualization.arrayToDataTable(dataReport);

    const options = {
        title: 'Розподіл витрат за період',
        titleTextStyle: {
            // color: <string>,
            fontName: 'Segoe UI',
            fontSize: 20,
            bold: true,
            italic: false
        },
        chartArea: {left: 0, top: 30, 'width': '100%', 'height': '100%'}
    };

    const chart = new google.visualization.PieChart(document.getElementById('piechart'));

    chart.draw(data, options);
}