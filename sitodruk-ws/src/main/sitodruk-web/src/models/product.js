export default class Product {
    constructor(id,name, categoryName,price,active,dtoVersion) {
      this.id = id;
      this.name = name;
      this.categoryName = categoryName;
      this.price = price;
      this.active = active;
      this.dtoVersion = dtoVersion;
    }
  }