# Использование #

File f = new File("pathtofile.1CD");

DBManager manager = new DBManager();

DB db = manager.open(f);

Table t = db.getTable("USERS");

assertNotNull(t);

Records records = db.getTableScaner(t);

for(Map<String, Object> r : records) {

> assertNotNull(r);

> System.out.println(r.toString());

}

db.close();