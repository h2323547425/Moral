import sqlite3 

conn = sqlite3.connect('esg_database')
c = conn.cursor()

c.execute(''' CREATE TABLE IF NOT EXISTS As_You_Sow_data (
    `Fund_profile` VARCHAR(24) CHARACTER SET utf8,
    `Shareclass_name` VARCHAR(87) CHARACTER SET utf8,
    `Name_of_the_shareclass` VARCHAR(600) CHARACTER SET utf8)''')
