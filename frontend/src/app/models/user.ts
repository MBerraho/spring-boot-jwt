export class User {
    id: number;
    username: string;
    email: string;
    password: string;
    roles: Role[];
  
    constructor(id: number, username: string, email: string, password: string, roles: Role[]) {
      this.id = id;
      this.username = username;
      this.email = email;
      this.password = password;
      this.roles = roles;
    }
  }
  
  export class Role {
    id!: number;
    name: string;
  
    constructor(name: string) {
      this.name = name;
    }
  }
  