#DB
This is the service provides the Database access.

##Supported Operations
* QUERY
```yaml
{
  "namepace": "[namespace]",
  "type": "QUERY",
  "options": {
    "table": "[table_name]",
    "where": {
      "field": "[field_name]",
      "value": "[value]"
    }
  }
}
```
**Note:** We only support SELECT all and a simple where clause at this time.

* INSERT
```yaml
{
  "namepace": "[namespace]",
  "type": "INSERT",
  "options": {
    "table": "[table_name]",
    "values": {
      "[field_1]": "[value_1]"
      // Add each field and value that will be set.
    }
  }
}
```
**Note:** The field names must match what is defined in the table schema.

* UPDATE
```yaml
{
  "namepace": "[namespace]",
  "type": "UPDATE",
  "options": {
    "table": "[table_name]",
    "id": {
      "[field]": "[value]"
    },
    "values": {
      "[field_1]": "[value_1]"
      // Add each field and value that will be set.
    }
  }
}
```

* DELETE
```yaml
{
  "namepace": "[namespace]",
  "type": "DELETE",
  "options": {
    "table": "[table_name]",
    "id": {
      "[field_1]": "[value_1]"
    }
  }
}
```
**Note:** The delete only supports delete by id as specified by the field.

* DDL
```yaml
{
  "namepace": "[namespace]",
  "type": "DDL",
  "options": {
    // TBD
  }
}
```