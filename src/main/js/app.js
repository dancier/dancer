'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const when = require('when');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/api';

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {dancers: [], attributes: [], pageSize: 2, links: {}};
		this.updatePageSize = this.updatePageSize.bind(this);
		this.onCreate = this.onCreate.bind(this);
		this.onUpdate = this.onUpdate.bind(this);
		this.onDelete = this.onDelete.bind(this);
		this.onNavigate = this.onNavigate.bind(this);
	}

    // tag::follow-2[]
    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'dancers', params: {size: pageSize}}]
        ).then(dancerCollection => {
            return client({
                method: 'GET',
                path: dancerCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                this.links = dancerCollection.entity._links.self.href
                return dancerCollection;
            });
        }).then(dancerCollection => {
            return dancerCollection.entity._embedded.dancers.map(dancer =>
                client({
                    method: 'GET',
                    path: dancer._links.self.href
                })
                );
        }).then(dancerPromises => {
        return when.all(dancerPromises);
        }).done(dancers => {
            this.setState({
                dancers: dancers,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: this.links
            });
        });
    }
    // end::follow-2[]

	// tag::create[]
	onCreate(newDancer) {
	    const self = this;
		follow(client, root, ['dancers']).then(response => {
			return client({
				method: 'POST',
				path: response.entity._links.self.href,
				entity: newDancer,
				headers: {'Content-Type': 'application/json'}
			})
		}).then(response => {
			return follow(client, root, [{rel: 'dancers', params: {'size': self.state.pageSize}}]);
		}).done(response => {
			if (typeof response.entity._links.last !== "undefined") {
				this.onNavigate(response.entity._links.last.href);
			} else {
				this.onNavigate(response.entity._links.self.href);
			}
		});
	}
	// end::create[]

	// tag::update[]
	onUpdate(dancer, updatedDancer) {
		client({
			method: 'PUT',
			path: dancer.entity._links.self.href,
			entity: updatedDancer,
			headers: {
				'Content-Type': 'application/json',
				'If-Match': dancer.headers.Etag
			}
		}).done(response => {
			this.loadFromServer(this.state.pageSize);
		}, response => {
			if (response.status.code === 412) {
				alert('DENIED: Unable to update ' +
					dancer.entity._links.self.href + '. Your copy is stale.');
			}
		});
	}
	// end::update[]

	// tag::delete[]
	onDelete(dancer) {
		client({method: 'DELETE', path: dancer._links.self.href}).done(response => {
			this.loadFromServer(this.state.pageSize);
		});
	}
	// end::delete[]

	// tag::navigate[]
	onNavigate(navUri) {
		client({
		    method: 'GET',
			path: navUri
		}).then(dancerCollection => {
			this.links = dancerCollection.entity._links;

			return dancerCollection.entity._embedded.dancers.map(dancer =>
					client({
						method: 'GET',
						path: dancer._links.self.href
					})
			);
		}).then(dancerPromises => {
			return when.all(dancerPromises);
		}).done(dancers => {
			this.setState({
				dancers: dancers,
				attributes: Object.keys(this.schema.properties),
				pageSize: this.state.pageSize,
				links: this.links
			});
		});
	}
	// end::navigate[]

	// tag::update-page-size[]
	updatePageSize(pageSize) {
		if (pageSize !== this.state.pageSize) {
			this.loadFromServer(pageSize);
		}
	}
	// end::update-page-size[]

    // tag::follow-1[]
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }
    // end::follow-1[]

	render() {
		return (
			<div>
				<CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
				<DancerList dancers={this.state.dancers}
							  links={this.state.links}
							  pageSize={this.state.pageSize}
							  attributes={this.state.attributes}
							  onNavigate={this.onNavigate}
							  onUpdate={this.onUpdate}
							  onDelete={this.onDelete}
							  updatePageSize={this.updatePageSize}/>
			</div>
		)
	}
}

// tag::create-dialog[]
class CreateDialog extends React.Component {

	constructor(props) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

    handleSubmit(e) {
        e.preventDefault();
        const newDancer = {};
        this.props.attributes.forEach(attribute => {
            newDancer[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newDancer);
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>
            <p key={attribute}>
            <input type="text" placeholder={attribute} ref={attribute} className="field"/>
            </p>
    );
        return (
            <div>
                <a href="#createDancer">Create</a>

                <div id="createDancer" className="modalDialog">
               <div>
             <a href="#" title="Close" className="close">X</a>

            <h2>Create new dancer</h2>

        <form>
        {inputs}
        <button onClick={this.handleSubmit}>Create</button>
            </form>
            </div>
            </div>
            </div>
        )
    }
}
// end::create-dialog[]

// tag::update-dialog[]
class UpdateDialog extends React.Component {

    constructor(props) {
        super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const updateDancer = {};
        this.props.attributes.forEach(attribute => {
            updateDancer[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.dancer, updateDancer);
        window.location = "#";
    }
    render() {
        const inputs = this.props.attributes.map(attribute =>
			<p key={this.props.dancer.entity[attribute]}>
            				<input type="text" placeholder={attribute}
					   defaultValue={this.props.dancer.entity[attribute]}
            					   ref={attribute} className="field"/>
            			</p>
        );

		const dialogId = "updateDancer-" + this.props.dancer.entity._links.self.href;

		return (
			<div key={this.props.dancer.entity._links.self.href}>
				<a href={"#" + dialogId}>Update</a>
				<div id={dialogId} className="modalDialog">
					<div>
						<a href="#" title="Close" className="close">X</a>

						<h2>Update an dancer</h2>

						<form>
							{inputs}
							<button onClick={this.handleSubmit}>Update</button>
						</form>
					</div>
				</div>
			</div>
		)
}

};
// end::update-dialog[]


class DancerList extends React.Component {

	constructor(props) {
		super(props);
		this.handleNavFirst = this.handleNavFirst.bind(this);
		this.handleNavPrev = this.handleNavPrev.bind(this);
		this.handleNavNext = this.handleNavNext.bind(this);
		this.handleNavLast = this.handleNavLast.bind(this);
		this.handleInput = this.handleInput.bind(this);
	}

	// tag::handle-page-size-updates[]
	handleInput(e) {
		e.preventDefault();
		const pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
		if (/^[0-9]+$/.test(pageSize)) {
			this.props.updatePageSize(pageSize);
		} else {
			ReactDOM.findDOMNode(this.refs.pageSize).value = pageSize.substring(0, pageSize.length - 1);
		}
	}
	// end::handle-page-size-updates[]

	// tag::handle-nav[]
	handleNavFirst(e){
		e.preventDefault();
		this.props.onNavigate(this.props.links.first.href);
	}
	handleNavPrev(e) {
		e.preventDefault();
		this.props.onNavigate(this.props.links.prev.href);
	}
	handleNavNext(e) {
		e.preventDefault();
		this.props.onNavigate(this.props.links.next.href);
	}
	handleNavLast(e) {
		e.preventDefault();
		this.props.onNavigate(this.props.links.last.href);
	}
	// end::handle-nav[]
	// tag::dancer-list-render[]
	render() {
		const dancers = this.props.dancers.map(dancer =>
			<Dancer key={dancer.entity._links.self.href}
			        dancer={dancer}
			        attributes={this.props.attributes}
			        onUpdate={this.props.onUpdate}
			        onDelete={this.props.onDelete}/>
		);

		const navLinks = [];
		if (this.props.links) {
			if (this.props.links.first) {
				navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
			}
			if (this.props.links.prev) {
				navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
			}
			if (this.props.links.next) {
				navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
			}
			if (this.props.links.last) {
				navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
			}
		}

		return (
			<div>
				<input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
				<table>
					<tbody>
						<tr>
							<th>First Name</th>
							<th>Last Name</th>
							<th>Description</th>
							<th></th>
							<th></th>
						</tr>
						{dancers}
					</tbody>
				</table>
				<div>
					{navLinks}
				</div>
			</div>
		)
	}
	// end::dancer-list-render[]
}

// tag::dancer[]
class Dancer extends React.Component {

	constructor(props) {
		super(props);
		this.handleDelete = this.handleDelete.bind(this);
	}

	handleDelete() {
		this.props.onDelete(this.props.dancer);
	}

	render() {
		return (
			<tr>
				<td>{this.props.dancer.entity.firstName}</td>
				<td>{this.props.dancer.entity.lastName}</td>
				<td>{this.props.dancer.entity.dance}</td>
				<td>
				    <UpdateDialog dancer={this.props.dancer}
				                  attributes={this.props.attributes}
				                  onUpdate={this.props.onUpdate} />
				</td>

				<td>
					<button onClick={this.handleDelete}>Delete</button>
				</td>
			</tr>
		)
	}
}
// end::dancer[]

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
