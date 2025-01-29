import { Layout, Menu } from "antd";
import { Link } from "react-router-dom";

const { Header } = Layout;

const Navbar = () => {
  return (
    <Header>
      <Menu theme="dark" mode="horizontal">
        <Menu.Item key="1">
          <Link to="/">Museos</Link>
        </Menu.Item>
        <Menu.Item key="2">
          <Link to="/curadores">Curadores</Link>
        </Menu.Item>
      </Menu>
    </Header>
  );
};

export default Navbar;
