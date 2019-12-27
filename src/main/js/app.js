// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
// end::vars[]

// tag.:app[]
class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {dancers: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/dancers'}).done(response => {
            this.setState({dancers: response.entity._embedded.dancers});
        });
    }

    render() {
        return (
            <DancerList dancers={this.state.dancers}/>
    )
    }
}

// end::app[]


// tag::dancer-list[]
class DancerList extends React.Component{
    render() {
        const dancers = this.props.dancers.map(dancer =>
            <Dancer key={dancer._links.self.href} dancer={dancer}/>
    );
        return (
            <table>
            <tbody>
            <tr>
            <th>First Name</th>
            <th>Last Name</th>
             <th>Dance</th>
              </tr>
        {dancers}
    </tbody>
        </table>
    )
    }
}
// end::dancer-list[]


// tag::dancer[]
class Dancer extends React.Component{
    render() {
        return (
            <tr>
            <td>{this.props.dancer.firstName}</td>
            <td>{this.props.dancer.lastName}</td>
            <td>{this.props.dancer.description}</td>
            </tr>
    )
    }
}
// end::employee[]


// tag::render[]
ReactDOM.render(
<App />,
    document.getElementById('react')
)
// end::render[]
